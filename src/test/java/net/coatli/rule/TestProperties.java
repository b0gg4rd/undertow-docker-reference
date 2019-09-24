package net.coatli.rule;

import static java.lang.String.format;

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

    try (final var inputStream = TestProperties.class.getResourceAsStream("/conf/test.properties")) {
      this.props = new Properties();
      this.props.load(inputStream);
    }

    LOGGER.info(format("test.properties '%s'", props));

  }

}
