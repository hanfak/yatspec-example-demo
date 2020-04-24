package learning.databaseservice;


import org.flywaydb.core.Flyway;

public class DatabaseMigration {
  // Can also do this via maven plugin
  // Should be done before app starts
  public void execute() {
    Flyway flyway = Flyway.configure()
            .dataSource("jdbc:postgresql://127.0.0.1:5432/starwarslocal",
                    "postgres",
                    "docker")
            .schemas("records")
            .load();
    flyway.migrate();
  }

  public static void main(String... args) {
    new DatabaseMigration().execute();
  }
}
