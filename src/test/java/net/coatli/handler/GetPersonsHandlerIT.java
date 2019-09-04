package net.coatli.handler;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.ClassRule;
import org.junit.Test;

import net.coatli.rule.TestProperties;

public class GetPersonsHandlerIT {

  @ClassRule
  public static TestProperties TEST_PROPERTIES = new TestProperties();

  private final String url = TEST_PROPERTIES.props().getProperty("get.persons.test.url");

  private final CloseableHttpClient httpClient = HttpClients.createDefault();

  @Test
  public void thatGetPersonsReturn200Works() throws Exception {
    // given
    final HttpGet request = new HttpGet(url);

    // when
    final CloseableHttpResponse response = httpClient.execute(request);

    // then
    assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
  }

}
