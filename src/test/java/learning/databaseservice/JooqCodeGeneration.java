package learning.databaseservice;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JooqCodeGeneration {

  // Can use maven plugin
  // if we have code that exists,then compile errors can occur
  // Done after flyway migration
  public static void execute() throws Exception {
    Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl("jdbc:postgresql://127.0.0.1:5432/starwarslocal")
                    .withUser("postgres")
                    .withPassword("docker"))
            .withGenerator(new Generator()
                    .withDatabase(new Database()
                            .withName("org.jooq.meta.postgres.PostgresDatabase")
                            .withIncludes(".*")
                            .withExcludes("")
                            .withInputSchema("records"))
                    .withTarget(new Target()
                            .withPackageName("org.jooq.sources")
                            .withDirectory("target/generated-sources/jooq")));

    GenerationTool.generate(configuration);
  }

  public static void main(String... args) throws Exception {
    JooqCodeGeneration.execute();
  }
}
