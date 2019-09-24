package net.coatli.handler;

import static io.undertow.util.Headers.CONTENT_TYPE;
import static io.undertow.util.StatusCodes.INTERNAL_SERVER_ERROR;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.coatli.util.PersonsHeaders.TEXT_PLAIN_UTF8;
import static net.coatli.util.PersonsHeaders.TRACE_ID;
import static net.coatli.util.PersonsHeaders.TRACE_ID_HTTPSTRING;
import static net.coatli.util.PersonsResponses.INTERNAL_SERVER_ERROR_MESSAGE;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class TraceHandler implements HttpHandler {

  private static final Logger LOGGER = LogManager.getLogger(TraceHandler.class);

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

  private final HttpHandler next;

  /**
   * Explicit default constructor with dependencies.
   *
   * @param next An type that impĺement {@link HttpHandler} for the handler's chain.
   */
  public TraceHandler(final HttpHandler next) {
    this.next = next;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(final HttpServerExchange exchange) throws Exception {

    exchange.dispatch(EXECUTOR, () -> {

      var traceId = "";
      var result  = "";

      try {

        exchange.getResponseHeaders().put(CONTENT_TYPE, TEXT_PLAIN_UTF8);

        // validating
        traceId = exchange.getRequestHeaders().getLast(TRACE_ID);

        if (traceId == null || traceId.isBlank()) {
          traceId = randomUUID().toString();
          exchange.getRequestHeaders().put(TRACE_ID_HTTPSTRING, traceId);
        }

        LOGGER.info("{} '{}'", TRACE_ID, traceId);

        next.handleRequest(exchange);

      } catch (final Exception exc) {
        result = format(INTERNAL_SERVER_ERROR_MESSAGE, traceId);
        LOGGER.error(format("Return '%s' '%s' '%s'", INTERNAL_SERVER_ERROR, exc.toString(), result), exc);
        exchange.setStatusCode(INTERNAL_SERVER_ERROR)
                .getResponseSender().send(result);

      }

    });

  }

}
