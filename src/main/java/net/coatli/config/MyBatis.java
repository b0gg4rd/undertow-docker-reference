package net.coatli.config;

import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.coatli.domain.Person;
import net.coatli.persistence.PersonsMapper;

public final class MyBatis {

  private static final Logger LOGGER = LogManager.getLogger(MyBatis.class);

  private static final String HIKARI_PROPERTIES = "/conf/hikari.properties";

  private static volatile SqlSessionFactory SQL_SESSION_FACTORY = null;

  /**
   * Private explicit default constructor for {@literal Singleton} pattern.
   */
  private MyBatis() {
  }

  public static SqlSessionFactory sqlSessionFactory() {

    if (SQL_SESSION_FACTORY == null) {

      SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(configuration());

      LOGGER.info("'{}' initialized", SQL_SESSION_FACTORY);
    }

    return SQL_SESSION_FACTORY;
  }

  private static Configuration configuration() {

    try (InputStream inputStream = MyBatis.class.getResourceAsStream(HIKARI_PROPERTIES)) {

      final Properties hikariConfig = new Properties();
      hikariConfig.load(inputStream);

      final Configuration configuration = new Configuration(
          new Environment("production",
                          new JdbcTransactionFactory(),
                          new HikariDataSource(new HikariConfig(hikariConfig))));

      // Aliases
      configuration.getTypeAliasRegistry().registerAlias(Person.class.getSimpleName(), Person.class);

      // Mappers
      configuration.addMapper(PersonsMapper.class);

      return configuration;
    } catch (final Exception exc) {
      LOGGER.error("Can not create SQL Session Factory", exc);
      throw new RuntimeException(exc.toString(), exc);
    }

  }

}
