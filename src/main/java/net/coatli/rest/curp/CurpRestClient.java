package net.coatli.rest.curp;

import static com.jsoniter.JsonIterator.deserialize;
import static io.undertow.util.StatusCodes.OK;
import static java.net.URI.create;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static net.coatli.UndertowDockerReferenceApplication.APPLICATION;

import net.coatli.rest.curp.domain.CurpRestRequest;
import net.coatli.rest.curp.domain.CurpRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CurpRestClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(CurpRestClient.class);

  public static CurpRestResponse consumeCurpDetails(final String curp) throws Exception {

    final var requestBody = new CurpRestRequest().setCurp(curp)
                                                 .setDocumento("0")
                                                 .toString();

    final var httpRequest = newBuilder().POST(ofString(requestBody))
                                        .uri(create(APPLICATION.getProperty("curp.url")))
                                        .build();

    LOGGER.info("{} '{}' '{}'", httpRequest.method(), httpRequest.uri(), requestBody);

    final var httpResponse = newHttpClient().send(httpRequest, ofString());

    LOGGER.info("'{}' '{}'", httpResponse.statusCode(), httpResponse.body());

    if (OK == httpResponse.statusCode()) {
      final var curpRestResponse = deserialize(httpResponse.body(), CurpRestResponse.class);

      if ("0".equalsIgnoreCase(curpRestResponse.getCodigoMensaje())) {
        return curpRestResponse;
      }
    }

    return null;
  }

}
