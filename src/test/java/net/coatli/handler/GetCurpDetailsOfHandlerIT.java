package com.chaintity.handler;

import static com.chaintity.util.CurpHeaders.V1;
import static com.chaintity.util.CurpHeaders.VALIDITY_DIGIT;
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

import com.chaintity.domain.CurpDetails;
import com.chaintity.rule.TestProperties;

public class GetCurpDetailsOfHandlerIT {

  @ClassRule
  public static TestProperties TEST_PROPERTIES = new TestProperties();

  private final String url = TEST_PROPERTIES.props().getProperty("get.curp.detailsof.test.url");

  private final CloseableHttpClient httpClient = HttpClients.createDefault();

  @Test
  public void thatEmptyCurpReturns400Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, ""));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatCurpWithInvalidFormatReturns400Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "ABCD1234"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatInvalidCurpReturns404Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL01"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatInvalidCurpReturns400Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL02"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatInvalidCurpReturns422Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL03"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatInvalidCurpReturnsNotNullUnprocessableCodeHeaderWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL03"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertNotEquals(null, response.getFirstHeader(X_UNPROCESSABLE_CODE));
  }

  @Test
  public void thatInvalidCurpReturnsCorrectUnprocessableCodeHeaderWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL03"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(VALIDITY_DIGIT, response.getFirstHeader(X_UNPROCESSABLE_CODE).getValue());
  }

  @Test
  public void thatRenapoDownFromUpstreamReturns500Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL04"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatEmptyResponseFromUpstreamReturns500Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL00"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
  }

  @Test
  public void that401FromUpstreamReturns500Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL99"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
  }

  @Test
  public void that504FromUpstreamReturns500Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFLCL98"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatValidCurpReturns200Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFCLC06"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatValidCurpReturnsNotNullWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFCLC06"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertNotEquals(null, deserialize(EntityUtils.toString(response.getEntity()), CurpDetails.class));
  }

  @Test
  public void thatValidCurpReturnsValidDetailsWorks() throws Exception {
    // given
    final HttpGet request = new HttpGet(format(url, "MAFR790428HDFCLC06"));
    addRequiredHeaders(request);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    final CurpDetails curpDetails = deserialize(EntityUtils.toString(response.getEntity()), CurpDetails.class);
    assertEquals(true, "Ricardo Daniel".equalsIgnoreCase(curpDetails.getNames()) &&
                       "Macedo".equalsIgnoreCase(curpDetails.getFirstSurname()) &&
                       "Flores".equalsIgnoreCase(curpDetails.getSecondSurname()) &&
                       "28/04/1979".equals(curpDetails.getBirthday()) &&
                       "Ciudad de Mexico".equalsIgnoreCase(curpDetails.getState().getDescription()) &&
                       "Hombre".equalsIgnoreCase(curpDetails.getGender().getDescription()));
  }

  private void addRequiredHeaders(final HttpGet request) {

    request.setHeader(X_VERSION, V1);
    request.setHeader(X_CLIENT_ID, TEST_PROPERTIES.props().getProperty("xclientid"));
    request.setHeader(X_TIME_LOCAL, TEST_PROPERTIES.props().getProperty("xtimelocal"));
    request.setHeader(X_FORWARDED_FOR, TEST_PROPERTIES.props().getProperty("xforwarderfor"));

  }

}
