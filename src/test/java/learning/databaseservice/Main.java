package learning.databaseservice;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;


public class Main {
  public static void main(String... args) {
    DataSource dataSource = DatasourceConfig.createDataSource();
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);

    dslContext.deleteFrom(CHARACTERINFO).execute();

    Optional<Record1<Integer>> result = dslContext.select(CHARACTERS.PERSON_ID)
            .from(CHARACTERS)
            .where(CHARACTERS.PERSON_NAME.eq("Luke Skywalker"))
            .fetchOptional();
    Integer id = result.map(Record1::component1).orElse(0);
    System.out.println("id = " + id);

    dslContext.selectFrom(CHARACTERS)
            //.forEach(x -> System.out.println(x.getPersonName() + ": " + x.getPersonId()));
            .forEach(System.out::println);

    System.out.println("Return all in table format");
    System.out.println(dslContext.selectFrom(CHARACTERS).fetch());

    System.out.println("Return any in table format");
    System.out.println(dslContext.selectFrom(CHARACTERS).fetchAny());

    System.out.println("Return all in individual row format");
    System.out.println(Arrays.toString(dslContext.selectFrom(CHARACTERS).fetchArray()));

    System.out.println("Return all in list of array format");
    List<Map<String, Object>> x = dslContext.selectFrom(CHARACTERS).fetchMaps();
    System.out.println(x);

    System.out.println();
    dslContext.insertInto(CHARACTERINFO)
            .set(CHARACTERINFO.PERSON_ID, id)
            .set(CHARACTERINFO.CHARACTER_INFO_ID, 1)
            .set(CHARACTERINFO.BIRTH_YEAR, "19 BBY")
            .execute();

    Record record = dslContext.select().from(CHARACTERINFO).fetchAny();
    System.out.println("record = " + record);
  }
}
