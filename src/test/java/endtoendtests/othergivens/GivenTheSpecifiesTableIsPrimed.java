package endtoendtests.othergivens;

import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import endtoendtests.database.TestDataProvider;

public class GivenTheSpecifiesTableIsPrimed implements GivensBuilder {

  // can set default here if not primed
  private String speciesName = "";
  private Integer lifespan = 10;
  private Float averageHeight = 1.0F;
  private Integer personId; // This will be null so set in the build method if not primed with with method
  private final TestDataProvider testDataProvider;

  public GivenTheSpecifiesTableIsPrimed(TestDataProvider testDataProvider) {
    this.testDataProvider = testDataProvider;
  }

  public static GivenTheSpecifiesTableIsPrimed givenTheSpecifiesTableIsPrimed(TestDataProvider testDataProvider) {
    return new GivenTheSpecifiesTableIsPrimed(testDataProvider);
  }

  public GivenTheSpecifiesTableIsPrimed withSpeciesName(String name) {
    this.speciesName = name;
    return this;
  }

  public GivenTheSpecifiesTableIsPrimed withLifespan(Integer lifespan) {
    this.lifespan = lifespan;
    return this;
  }

  public GivenTheSpecifiesTableIsPrimed withAverageHeight(Float averageHeight) {
    this.averageHeight = averageHeight;
    return this;
  }

  public GivenTheSpecifiesTableIsPrimed withPersonId(Integer personId) {
    this.personId = personId;
    return this;
  }


  @Override
  public InterestingGivens build(InterestingGivens givens) throws Exception {
    // Can set default here, if not primed, as it needs to get info from InterestingGivens
    if (personId == null) {
      personId = givens.getType(Person.class).getPersonId();
    }
    testDataProvider.storeSpecifiesInfo(personId, speciesName, averageHeight, lifespan);
    return givens;
  }
}
