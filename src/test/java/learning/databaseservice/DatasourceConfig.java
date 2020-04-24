package learning.databaseservice;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatasourceConfig {
  private DatasourceConfig() {
  }

  public static DataSource createDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/starwarslocal");
    config.setUsername("postgres");
    config.setPassword("docker");
    config.setAutoCommit(true);
    config.setMaximumPoolSize(10);
    return new HikariDataSource(config);
  }
}
