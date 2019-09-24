package net.coatli.handler;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;

import org.junit.ClassRule;
import org.junit.Test;

import net.coatli.rule.TestProperties;

public class GetPersonsHandlerIT {

  @ClassRule
  public static TestProperties TEST_PROPERTIES = new TestProperties();

  private final String url = TEST_PROPERTIES.props().getProperty("get.persons.test.url");

  private final HttpClient client = newHttpClient();

  @Test
  public void thatGetPersonsReturn200Works() throws Exception {
    // given
    final var request = newBuilder(URI.create(url)).GET().build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(HTTP_OK, response.statusCode());
  }

}
