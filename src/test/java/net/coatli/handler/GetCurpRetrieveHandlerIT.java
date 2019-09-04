package com.chaintity.handler;

import static com.chaintity.util.CurpHeaders.INVALID_DETAILS;
import static com.chaintity.util.CurpHeaders.V1;
import static com.chaintity.util.CurpHeaders.X_UNPROCESSABLE_CODE;
import static com.chaintity.util.CurpHeaders.X_VERSION;
import static com.chaintity.util.TracesSourceHeaders.X_CLIENT_ID;
import static com.chaintity.util.TracesSourceHeaders.X_FORWARDED_FOR;
import static com.chaintity.util.TracesSourceHeaders.X_TIME_LOCAL;
import static com.jsoniter.JsonIterator.deserialize;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.ClassRule;
import org.junit.Test;

import com.chaintity.domain.Curp;
import com.chaintity.rule.TestProperties;

public class GetCurpRetrieveHandlerIT {

  @ClassRule
  public static TestProperties TEST_PROPERTIES = new TestProperties();

  private final String curpUrl     = TEST_PROPERTIES.props().getProperty("get.curp.test.url");
  private final String retrieveUrl = TEST_PROPERTIES.props().getProperty("get.curp.retrieve.test.url");

  private final CloseableHttpClient httpClient = HttpClients.createDefault();

  @Test
  public void thatDetailsWithOnlyNameReturns501Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(curpUrl + "?name=Rafael");
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatDetailsWithOnlyFirstSurnameReturns501Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(curpUrl + "?firstSurname=Morales");
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatDetailsWithOnlySecondSurnameReturns501Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(curpUrl + "?secondSurname=Parra");
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatDetailsWithOnlyBirthdayReturns501Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(curpUrl + "?birthday=20/10/1975");
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatDetailsWithOnlyStateReturns501Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(curpUrl + "?state=DF");
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatDetailsWithOnlyGenderReturns501Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(curpUrl + "?gender=H");
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, response.getStatusLine().getStatusCode());
  }

  @Test
  public void that401FromUpstreamReturns500Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Unauthorized", "Unauthorized", "Unauthorized", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
  }

  @Test
  public void that504FromUpstreamReturns500Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Timeout", "Timeout", "Timeout", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatInvalidDetailsFromUpstreamReturns422Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Invalid", "Details", "Upstream", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatInvalidDetailsFromUpstreamReturnsNotNullUnprocessableCodeWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Invalid", "Details", "Upstream", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertNotEquals(null, response.getFirstHeader(X_UNPROCESSABLE_CODE));
  }

  @Test
  public void thatInvalidDetailsFromUpstreamReturnsCorrectUnprocessableCodeWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Invalid", "Details", "Upstream", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(INVALID_DETAILS, response.getFirstHeader(X_UNPROCESSABLE_CODE).getValue());
  }

  @Test
  public void thatPersonNotExistsFromUpstreamReturns404Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Person", "NotExists", "Upstream", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatPersonWithHomonimiasFromUpstreamReturns409Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Person", "Homonimias", "Upstream", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_CONFLICT, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatRenapoDownFromUpstreamReturns500Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Renapo", "Down", "Upstream", "29/02/1900", "DF", "M"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatValidDetailsReturnsNotNullModelWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Rafael", "Morales", "Parra", "20/10/1975", "DF", "H"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertNotEquals(null, deserialize(EntityUtils.toString(response.getEntity()), Curp.class));
  }

  @Test
  public void thatValidDetailsReturnsValidCurpWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(retrieveUrl, "Rafael", "Morales", "Parra", "20/10/1975", "DF", "H"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals("MOPR751020HDFRRF04",  deserialize(EntityUtils.toString(response.getEntity()), Curp.class).getValue());
  }

  private void addRequiredHeaders(final HttpGet request) {

    request.setHeader(X_VERSION, V1);
    request.setHeader(X_CLIENT_ID, TEST_PROPERTIES.props().getProperty("xclientid"));
    request.setHeader(X_TIME_LOCAL, TEST_PROPERTIES.props().getProperty("xtimelocal"));
    request.setHeader(X_FORWARDED_FOR, TEST_PROPERTIES.props().getProperty("xforwarderfor"));

  }

}
