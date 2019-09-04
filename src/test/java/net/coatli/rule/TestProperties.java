package com.chaintity.rule;

import static java.lang.String.format;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.rules.ExternalResource;

public class TestProperties extends ExternalResource {

  private static final Logger LOGGER = Logger.getLogger(TestProperties.class.getName());

  private Properties props;

  public Properties props() {
    return props;
  }

  @Override
  protected void before() throws Throwable {

    try (InputStream inputStream = TestProperties.class.getResourceAsStream("/conf/test.properties")) {
      this.props = new Properties();
      this.props.load(inputStream);
    }

    props.put("xtimelocal", new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z").format(Calendar.getInstance().getTime()));

    LOGGER.info(format("test.properties '%s'", props));

  }

}
