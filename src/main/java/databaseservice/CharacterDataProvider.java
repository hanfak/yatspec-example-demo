package databaseservice;

import domain.Person;
import domain.SpeciesInfo;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Optional;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class CharacterDataProvider implements DataProvider {

  private final DataSource dataSource = DatasourceConfig.createDataSource();

  @Override
  public Integer getPersonId(String personName) {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    Optional<Record1<Integer>> result = dslContext.select(CHARACTERS.PERSON_ID)
            .from(CHARACTERS)
            .where(CHARACTERS.PERSON_NAME.eq(personName))
            .fetchOptional();
    return result.map(Record1::component1).orElse(0);
  }

  @Override
  public void storeCharacterInfo(String personId, Person characterInfo) {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.insertInto(CHARACTERINFO)
            .set(CHARACTERINFO.PERSON_ID, Integer.parseInt(personId))
            .set(CHARACTERINFO.BIRTH_YEAR, characterInfo.getBirthYear())
            .set(CHARACTERINFO.PERSON_NAME, characterInfo.getName())
            .execute();
  }

  @Override
  public SpeciesInfo getSpeciesInfo(Integer personId) {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    Optional<Record3<String, Float, Integer>> result = dslContext.select(SPECIFIESINFO.SPECIES, SPECIFIESINFO.AVG_HEIGHT, SPECIFIESINFO.LIFESPAN)
            .from(SPECIFIESINFO)
            .where(SPECIFIESINFO.PERSON_ID.eq(personId))
            .fetchOptional();
    String name = result.get().field1().getName();
    System.out.println("name = " + name);
    return result.map(record -> new SpeciesInfo(record.component1(), record.component3(), record.component2())).orElseThrow(IllegalStateException::new);
  }
}
