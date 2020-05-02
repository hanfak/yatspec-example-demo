package endtoendtests.fluentthen.thens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import endtoendtests.database.TestDataProvider;
import endtoendtests.helper.CharacterInfo;
import endtoendtests.helper.Person;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class ThenTheDatabase {
  private final TestState interactions;
  private final TestDataProvider testDataProvider;

  public ThenTheDatabase(TestState interactions, TestDataProvider testDataProvider) {
    this.interactions = interactions;
    this.testDataProvider = testDataProvider;
  }

  // Here are uses of TestState is done here, only assertions is done in DatabaseMatcher
  public DatabaseMatcher theDatabase() {
    Person person = interactions.interestingGivens.getType(Person.class);
    // Need to get the data that was populated by the app under test, should only be method used in test
    CharacterInfo characterInfo = testDataProvider.getCharacterInfo(person.getPersonId());
    // Want the output to be logged, it is an output of the application, so should not be part of interestinggivens
    interactions.capturedInputAndOutputs.add("Database State: CharacterInfo", prettyObject(characterInfo));
    return new DatabaseMatcher(characterInfo);
  }

  private String prettyObject(Object event) {
    return ToStringBuilder.reflectionToString(event, MULTI_LINE_STYLE);
  }
}
