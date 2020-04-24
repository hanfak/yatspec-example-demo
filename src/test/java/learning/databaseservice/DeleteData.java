package learning.databaseservice;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERINFO;

public class DeleteData {
  public static void main(String... args) {
    DataSource dataSource = DatasourceConfig.createDataSource();
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);

    dslContext.deleteFrom(CHARACTERINFO).execute();
  }
}
