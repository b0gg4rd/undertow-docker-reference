package net.coatli.handler;

import static com.chaintity.util.CurpHeaders.V1;
import static com.chaintity.util.CurpHeaders.X_VERSION;
import static com.chaintity.util.TracesSourceHeaders.X_CLIENT_ID;
import static com.chaintity.util.TracesSourceHeaders.X_FORWARDED_FOR;
import static com.chaintity.util.TracesSourceHeaders.X_TIME_LOCAL;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.ClassRule;
import org.junit.Test;

import net.coatli.rule.TestProperties;

public class GetCurpHandlerIT {

  @ClassRule
  public static TestProperties TEST_PROPERTIES = new TestProperties();

  private final String url = TEST_PROPERTIES.props().getProperty("get.curp.test.url");

  private final CloseableHttpClient httpClient = HttpClients.createDefault();

  @Test
  public void thatEmptyVersionHeaderReturns400Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(url);
    request.setHeader(X_CLIENT_ID, TEST_PROPERTIES.props().getProperty("xclientid"));
    request.setHeader(X_TIME_LOCAL, TEST_PROPERTIES.props().getProperty("xtimelocal"));
    request.setHeader(X_FORWARDED_FOR, TEST_PROPERTIES.props().getProperty("xforwarderfor"));

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
  }

  @Test
  public void thatRequestWithoutQueryParamsReturns501Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(url);
    request.setHeader(X_VERSION, V1);
    request.setHeader(X_CLIENT_ID, TEST_PROPERTIES.props().getProperty("xclientid"));
    request.setHeader(X_TIME_LOCAL, TEST_PROPERTIES.props().getProperty("xtimelocal"));
    request.setHeader(X_FORWARDED_FOR, TEST_PROPERTIES.props().getProperty("xforwarderfor"));

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_NOT_IMPLEMENTED, response.getStatusLine().getStatusCode());
  }

}
