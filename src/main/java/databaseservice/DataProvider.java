package databaseservice;

import domain.Person;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Optional;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;

public class DataProvider {

  private final DataSource dataSource = DatasourceConfig.createDataSource();

  public Integer getPersonId(String personName) {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    Optional<Record1<Integer>> result = dslContext.select(CHARACTERS.PERSON_ID)
            .from(CHARACTERS)
            .where(CHARACTERS.PERSON_NAME.eq(personName))
            .fetchOptional();
    return result.map(Record1::component1).orElse(0);
  }

  public void storeCharacterInfo(String personId, Person characterInfo) {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.insertInto(CHARACTERINFO)
            .set(CHARACTERINFO.PERSON_ID, Integer.parseInt(personId))
            .set(CHARACTERINFO.BIRTH_YEAR, characterInfo.getBirthYear())
            .set(CHARACTERINFO.PERSON_NAME, characterInfo.getName())
            .execute();
  }
}
