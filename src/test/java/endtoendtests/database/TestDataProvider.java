package endtoendtests.database;

import databaseservice.DatasourceConfig;
import endtoendtests.helper.CharacterInfo;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Optional;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;
// TODO extend CharacterDataProvider, which is newed when app is started in test context
// TODO need to seperate factories into new class, n pass into app.start
//TODO settings for different db name, so prod is not used
public class TestDataProvider {
  private final DataSource dataSource = DatasourceConfig.createDataSource();

  public void storeSpecifiesInfo(Integer personId, String name, Float avgHeight, Integer lifeSpan){
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.insertInto(SPECIFIESINFO)
            .set(SPECIFIESINFO.PERSON_ID, personId)
            .set(SPECIFIESINFO.SPECIES, name)
            .set(SPECIFIESINFO.AVG_HEIGHT, avgHeight)
            .set(SPECIFIESINFO.LIFESPAN, lifeSpan)
            .execute();
  }

  public void storeCharacter(Integer personId, String name){
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.insertInto(CHARACTERS)
            .set(CHARACTERS.PERSON_ID, personId)
            .set(CHARACTERS.PERSON_NAME, name)
            .execute();
  }

  public void populateTables() {
    storeCharacter(1, "blah");
  }


  public void deleteAllInfo() {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.deleteFrom(CHARACTERINFO).execute();
    dslContext.deleteFrom(SPECIFIESINFO).execute();
  }

  public void deleteAllInfoFromAllTables() {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.deleteFrom(CHARACTERINFO).execute();
    dslContext.deleteFrom(SPECIFIESINFO).execute();
    dslContext.deleteFrom(CHARACTERS).execute();
  }

  public CharacterInfo getCharacterInfo(Integer personId) {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    Optional<Record3<Integer, String, String>> result = dslContext.select(CHARACTERINFO.PERSON_ID, CHARACTERINFO.PERSON_NAME, CHARACTERINFO.BIRTH_YEAR)
            .from(CHARACTERINFO)
            .where(CHARACTERINFO.PERSON_ID.eq(personId))
            .fetchOptional();
    return result.map(record -> new CharacterInfo(record.component1(), record.component2(), record.component3())).orElseThrow(IllegalStateException::new);
  }
}
