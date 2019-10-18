package net.coatli;

import static io.undertow.util.Methods.GET;
import static io.undertow.util.Methods.PATCH;
import static io.undertow.util.Methods.POST;
import static java.lang.Integer.parseInt;
import static java.lang.Runtime.getRuntime;
import static net.coatli.config.MyBatis.sqlSessionFactory;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xnio.Options;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import net.coatli.handler.GetPersonsHandler;
import net.coatli.handler.GetPersonsIdHandler;
import net.coatli.handler.PatchPersonsIdHandler;
import net.coatli.handler.PostPersonsHandler;
import net.coatli.handler.TraceHandler;

/**
 * Main class for the self-package application
 */
public class UndertowDockerReferenceApplication {

  public static final Properties APPLICATION = new Properties();

  private static final String APPLICATION_PROPERTIES = "/conf/application.properties";

  private static final String HOST           = "host";
  private static final String PORT           = "port";
  private static final int    IO_THREADS     = getRuntime().availableProcessors() * 4;
  private static final int    BUFFER_SIZE    = 1024 * 64;
  private static final int    BACKLOG        = 10000;
  private static final int    WORKER_THREADS = IO_THREADS * 2;

  private static final Logger LOGGER = LogManager.getLogger(UndertowDockerReferenceApplication.class);

  public static void main(final String[] args) throws Exception {

    try (final var inputStream = UndertowDockerReferenceApplication.class.getResourceAsStream(APPLICATION_PROPERTIES)) {

      APPLICATION.load(inputStream);

      sqlSessionFactory();

      Undertow.builder()
          .addHttpListener(parseInt(APPLICATION.getProperty(PORT)),
                           APPLICATION.getProperty(HOST))
          .setBufferSize(BUFFER_SIZE)
          .setIoThreads(IO_THREADS)
          .setWorkerThreads(WORKER_THREADS)
          .setSocketOption(Options.BACKLOG, BACKLOG)
          .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
          .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true)
          .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, false)
          .setHandler(routes())
        .build()
        .start();
    }

    LOGGER.info("{} started for interface {} and port {}", UndertowDockerReferenceApplication.class.getSimpleName(),
                                                           APPLICATION.getProperty(HOST),
                                                           APPLICATION.getProperty(PORT));

  }

  /**
   * Definition of the routes to dispatch.
   *
   * @return An instance of {@link HttpHandler} from {@link Handlers} {@code routing} with the routing.
   */
  private static HttpHandler routes() {
    return Handlers.routing()
        .add(GET  , "/api/v1/persons",            new TraceHandler(new GetPersonsHandler()))
        .add(POST , "/api/v1/persons",            new TraceHandler(new PostPersonsHandler()))
        .add(GET  , "/api/v1/persons/{personId}", new TraceHandler(new GetPersonsIdHandler()))
        .add(PATCH, "/api/v1/persons/{personId}", new TraceHandler(new PatchPersonsIdHandler()));
  }

}
