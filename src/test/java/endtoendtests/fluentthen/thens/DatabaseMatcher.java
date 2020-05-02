package endtoendtests.fluentthen.thens;

import endtoendtests.helper.CharacterInfo;
import org.assertj.core.api.AbstractObjectAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseMatcher extends AbstractObjectAssert<DatabaseMatcher, CharacterInfo> {

  public DatabaseMatcher(CharacterInfo characterInfo) {
    super(characterInfo, DatabaseMatcher.class);
  }

  public DatabaseMatcher hasBirthYear(String expectedBirthYear) {
    assertThat(actual.birthYear).isEqualTo(expectedBirthYear);
    return this;
  }

  public DatabaseMatcher hasCharacterInfo(CharacterInfo expectedCharacterInfo) {
    assertThat(actual).isEqualTo(expectedCharacterInfo);
    return this;
  }
}
