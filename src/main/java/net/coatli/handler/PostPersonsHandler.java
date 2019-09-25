package net.coatli.handler;

import static com.jsoniter.JsonIterator.deserialize;
import static io.undertow.util.Headers.CONTENT_TYPE;
import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static io.undertow.util.StatusCodes.CREATED;
import static io.undertow.util.StatusCodes.INTERNAL_SERVER_ERROR;
import static io.undertow.util.StatusCodes.UNPROCESSABLE_ENTITY;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.coatli.config.MyBatis.sqlSessionFactory;
import static net.coatli.rest.renapo.CurpRestClient.retrieveDetails;
import static net.coatli.util.PersonsHeaders.TEXT_PLAIN_UTF8;
import static net.coatli.util.PersonsHeaders.TRACE_ID;
import static net.coatli.util.PersonsHeaders.X_PERSON_ID;
import static net.coatli.util.PersonsHeaders.X_PERSON_ID_HTTPSTRING;
import static net.coatli.util.PersonsRequestBody.invalidPersonCreate;
import static net.coatli.util.PersonsResponses.INTERNAL_SERVER_ERROR_MESSAGE;
import static org.apache.logging.log4j.ThreadContext.put;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

public class PostPersonsHandler implements HttpHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(PostPersonsHandler.class);

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

  @Override
  public void handleRequest(final HttpServerExchange exchange) throws Exception {

    exchange.dispatch(EXECUTOR, () -> {

      var traceId = "";
      var result  = "";

      try {

        traceId = exchange.getRequestHeaders().getLast(TRACE_ID);

        put(TRACE_ID, traceId);

        exchange.getResponseHeaders().put(CONTENT_TYPE, TEXT_PLAIN_UTF8);

        // request body reading block
        final var stringBuilder = new StringBuilder();
        var line   = "";
        var person = new Person();

        exchange.startBlocking();

        try (final var bufferedReader = new BufferedReader(new InputStreamReader(exchange.getInputStream()))) {
          while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
          }

          if (stringBuilder.toString().isBlank()) {
            result = "Empty body, please read the OpenAPI";
            LOGGER.info("Return '{}' '{}' '{}'", BAD_REQUEST, stringBuilder.toString(), result);
            exchange.setStatusCode(BAD_REQUEST)
                    .getResponseSender().send(result);
            return ;
          }

          person = deserialize(stringBuilder.toString(), Person.class);

          if (invalidPersonCreate(person)) {
            result = "Invalid body, please read the OpenAPI";
            LOGGER.info("Return '{}' '{}' '{}'", BAD_REQUEST, person, result);
            exchange.setStatusCode(BAD_REQUEST)
                    .getResponseSender().send(result);
            return ;
          }

        } catch (final Exception exc) {
          result = "Unprocessable body, please read the OpenAPI";
          LOGGER.info(format("Return '%s' '%s' '%s'", UNPROCESSABLE_ENTITY, exc.toString(), result), exc);
          exchange.setStatusCode(UNPROCESSABLE_ENTITY)
                  .getResponseSender().send(result);
          return ;
        }

        final var response = retrieveDetails(person.getCurp());

        if (response == null) {
          result = format(INTERNAL_SERVER_ERROR_MESSAGE, traceId);
          LOGGER.info("Return '{}' many rows created '{}'", INTERNAL_SERVER_ERROR, result);
          exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR)
          .getResponseSender().send(result);
        }

        LOGGER.info("Creating person '{}'", person.setId(randomUUID().toString())
                                                  .setNames(response.getNombre())
                                                  .setFirstSurname(response.getApellidoPaterno())
                                                  .setSecondSurname(response.getApellidoMaterno())
                                                  .setGender(response.getSexo())
                                                  .setBirthday(response.getFechaNacimiento()));

        SqlSession sqlSession = null;

        try {

          sqlSession = sqlSessionFactory().openSession(false);

          if (sqlSession.getMapper(PersonsMapper.class).create(person) != 1) {
            sqlSession.rollback();
            result = format(INTERNAL_SERVER_ERROR_MESSAGE, traceId);
            LOGGER.info("Return '{}' many rows created '{}'", INTERNAL_SERVER_ERROR, result);
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR)
                    .getResponseSender().send(result);
          }

          sqlSession.commit();

        } catch (final Exception exc) {

          if (sqlSession != null) {
            sqlSession.rollback();
          }

          result = format(INTERNAL_SERVER_ERROR_MESSAGE, traceId);
          LOGGER.error(format("Return '%s' '%s' '%s'", INTERNAL_SERVER_ERROR, exc.toString(), result), exc);
          exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR)
                  .getResponseSender().send(result);

        } finally {
          if (sqlSession != null) {
            sqlSession.close();
          }
        }

        LOGGER.info("Return '{}' '{}' '{}'", CREATED, X_PERSON_ID, person.getId());
        exchange.getResponseHeaders().put(X_PERSON_ID_HTTPSTRING, person.getId());
        exchange.setStatusCode(CREATED)
                .endExchange();

      } catch (final Exception exc) {
        result = format(INTERNAL_SERVER_ERROR_MESSAGE, traceId);
        LOGGER.error(format("Return '%s' '%s' '%s'", INTERNAL_SERVER_ERROR, exc.toString(), result), exc);
        exchange.setStatusCode(INTERNAL_SERVER_ERROR)
                .getResponseSender().send(result);
      }

    });

  }

}
