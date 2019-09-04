package net.coatli.handler;

import static com.jsoniter.output.JsonStream.serialize;
import static io.undertow.util.Headers.CONTENT_TYPE;
import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static io.undertow.util.StatusCodes.INTERNAL_SERVER_ERROR;
import static io.undertow.util.StatusCodes.NOT_FOUND;
import static io.undertow.util.StatusCodes.OK;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.coatli.config.MyBatis.sqlSessionFactory;
import static net.coatli.util.PersonsHeaders.APPLICATION_JSON;
import static net.coatli.util.PersonsHeaders.TEXT_PLAIN_UTF8;
import static net.coatli.util.PersonsHeaders.TRACE_HEADER;
import static net.coatli.util.PersonsQueryParams.PERSON_ID;
import static net.coatli.util.PersonsResponses.INTERNAL_SERVER_ERROR_MESSAGE;
import static org.apache.logging.log4j.ThreadContext.put;

import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import net.coatli.domain.Person;
import net.coatli.persistence.PersonsMapper;

public class GetPersonIdHandler implements HttpHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetPersonsHandler.class);

  private static final int CORE_POOL_SIZE          = 8;
  private static final int MAXIMUM_POOL_SIZE       = 16;
  private static final int KEEP_ALIVE_TIME         = 200;
  private static final int BLOCKING_QUEUE_CAPACITY = 16;

  private static ExecutorService EXECUTOR = new ThreadPoolExecutor(
                                              CORE_POOL_SIZE,
                                              MAXIMUM_POOL_SIZE,
                                              KEEP_ALIVE_TIME,
                                              MILLISECONDS,
                                              new LinkedBlockingQueue<Runnable>(BLOCKING_QUEUE_CAPACITY),
                                              new ThreadPoolExecutor.CallerRunsPolicy());

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(final HttpServerExchange exchange) throws Exception {

    exchange.dispatch(EXECUTOR, () -> {

      final String traceId = randomUUID().toString();

      put(TRACE_HEADER, traceId);

      exchange.getResponseHeaders().put(CONTENT_TYPE, TEXT_PLAIN_UTF8);

      String result = "";

      // validations over request query or path parameters
      final Deque<String> personId = exchange.getQueryParameters().get(PERSON_ID);
      if (personId == null || personId.getLast().trim().isEmpty()) {
        result = format("The path variable '%s' is required.", PERSON_ID);
        LOGGER.info("Return '{}' '{}'", BAD_REQUEST, result);
        exchange.setStatusCode(BAD_REQUEST)
                .getResponseSender().send(result);
      }

      // microservice logic block
      try (final SqlSession sqlSession = sqlSessionFactory().openSession(true)) {

        LOGGER.info("Retrive person '{}'", personId.getLast());

        final Person person = sqlSession.getMapper(PersonsMapper.class).retrieveOne(personId.getLast());

        if (person == null) {
          LOGGER.info("Return '{}' '{}'", NOT_FOUND, personId);
          exchange.setStatusCode(NOT_FOUND)
                  .endExchange();
          return ;
        }

        result = serialize(person);
        LOGGER.info("Return '{}' '{}'", OK, result);
        exchange.getResponseHeaders().put(CONTENT_TYPE, APPLICATION_JSON);
        exchange.setStatusCode(OK)
                .getResponseSender().send(result);

      } catch (final Exception exc) {
        result = format(INTERNAL_SERVER_ERROR_MESSAGE, traceId);
        LOGGER.error(format("Return '%s' '%s' '%s'", INTERNAL_SERVER_ERROR, exc.toString(), result), exc);
        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR)
                .getResponseSender().send(result);
      }

    });

  }

}
