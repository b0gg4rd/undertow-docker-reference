package net.coatli.rest.renapo;

import static com.jsoniter.JsonIterator.deserialize;
import static io.undertow.util.StatusCodes.OK;
import static java.net.URI.create;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static net.coatli.UndertowDockerReferenceApplication.APPLICATION;

public final class CurpRestClient {

  public static CurpRestResponse retrieveDetails(final String curp) throws Exception {

    final var httpResponse = newHttpClient().send(
                                         newBuilder(create(APPLICATION.getProperty("renapo.url"))).build(), ofString());

    if (OK == httpResponse.statusCode()) {
      final var curpRestResponse = deserialize(httpResponse.body(), CurpRestResponse.class);

      if ("0".equalsIgnoreCase(curpRestResponse.getCodigoMensaje())) {
        return curpRestResponse;
      }
    }

    return null;
  }

}
