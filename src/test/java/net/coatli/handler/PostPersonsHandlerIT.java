package net.coatli.handler;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.URI.create;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.junit.Assert.assertEquals;

import java.net.http.HttpClient;

import org.junit.ClassRule;
import org.junit.Test;

import net.coatli.domain.Person;
import net.coatli.rule.TestProperties;

public class PostPersonsHandlerIT {

  @ClassRule
  public static TestProperties TEST_PROPERTIES = new TestProperties();

  private final String url = TEST_PROPERTIES.props().getProperty("post.persons.test.url");

  private final HttpClient client = newHttpClient();

  @Test
  public void thatEmptyBodyReturns400Works() throws Exception {
    // given
    final var request = newBuilder(create(url)).POST(ofString(""))
                                               .build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(HTTP_BAD_REQUEST, response.statusCode());
  }

  @Test
  public void thatInvalidBodyReturns400Works() throws Exception {
    // given
    final var request = newBuilder(create(url)).POST(ofString("{}"))
                                               .build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(HTTP_BAD_REQUEST, response.statusCode());
  }

  @Test
  public void thatUnprocessableBodyReturns422Works() throws Exception {
    // given
    final var request = newBuilder(create(url)).POST(ofString("{_}"))
                                               .build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(422, response.statusCode());
  }

  @Test
  public void thatValidBodyReturns201Works() throws Exception {
    // given
    final var request = newBuilder(create(url)).POST(ofString(new Person().setCurp("MAFR790428HDFCLC06").toString()))
                                               .build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(HTTP_CREATED, response.statusCode());
  }

  @Test
  public void thatValidBodyWithInvalidContentReturns500Works() throws Exception {
    // given
    final var request = newBuilder(create(url)).POST(ofString(new Person().setCurp("MAFR790428HDFLCL01").toString()))
                                               .build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(HTTP_INTERNAL_ERROR, response.statusCode());
  }

  @Test
  public void thatUpstreamResponse504Returns500Works() throws Exception {
    // given
    final var request = newBuilder(create(url)).POST(ofString(new Person().setCurp("MAFR790428HDFLCL98").toString()))
                                               .build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(HTTP_INTERNAL_ERROR, response.statusCode());
  }

}
