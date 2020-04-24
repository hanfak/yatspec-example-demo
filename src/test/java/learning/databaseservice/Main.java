package learning.databaseservice;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Optional;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;


public class Main {
  public static void main(String... args) {
    DataSource dataSource = DatasourceConfig.createDataSource();
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);

    dslContext.delete(CHARACTERINFO).execute();

    Optional<Record1<Integer>> result = dslContext.select(CHARACTERS.PERSON_ID)
            .from(CHARACTERS)
            .where(CHARACTERS.PERSON_NAME.eq("Luke"))
            .fetchOptional();
    Integer id = result.map(Record1::component1).orElse(0);
    System.out.println("id = " + id);

    dslContext.insertInto(CHARACTERINFO)
            .set(CHARACTERINFO.PERSON_ID, id)
            .set(CHARACTERINFO.CHARACTER_INFO_ID, 1)
            .set(CHARACTERINFO.BIRTH_YEAR, "19 BBY")
            .execute();

    Record record = dslContext.select().from(CHARACTERINFO).fetchAny();
    System.out.println("record = " + record);
  }
}
