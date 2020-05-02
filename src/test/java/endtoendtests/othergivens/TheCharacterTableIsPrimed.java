package endtoendtests.othergivens;

import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import endtoendtests.database.TestDataProvider;
import endtoendtests.helper.Person;

public class TheCharacterTableIsPrimed implements GivensBuilder {

  private final TestDataProvider testDataProvider;
  private String characterName;
  private Integer personId;

  public TheCharacterTableIsPrimed(TestDataProvider testDataProvider) {
    this.testDataProvider = testDataProvider;
  }

  public static TheCharacterTableIsPrimed theCharacterTableIsPrimed(TestDataProvider testDataProvider) {
    return new TheCharacterTableIsPrimed(testDataProvider);
  }

  public TheCharacterTableIsPrimed withPersonId(Integer personId) {
    this.personId = personId;
    return this;
  }

  public TheCharacterTableIsPrimed withCharacterName(String characterName) {
    this.characterName = characterName;
    return this;
  }

  // The interesting givens is populated here rather than the test
  @Override
  public InterestingGivens build(InterestingGivens givens) throws Exception {
    testDataProvider.storeCharacter(personId, characterName);
    Person person = new Person(personId, characterName);
    return givens.add(person);
  }
}
