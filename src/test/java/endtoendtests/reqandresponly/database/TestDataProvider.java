package endtoendtests.reqandresponly.database;

import databaseservice.DatasourceConfig;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERINFO;
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

  public void deleteAllInfo() {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.deleteFrom(CHARACTERINFO).execute();
    dslContext.deleteFrom(SPECIFIESINFO).execute();
  }

}
