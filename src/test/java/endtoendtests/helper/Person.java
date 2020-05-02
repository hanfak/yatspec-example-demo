package endtoendtests.helper;

import java.util.StringJoiner;

public class Person {
  private final Integer personId;
  private final String personName;

  public Person(Integer personId, String personName) {
    this.personId = personId;
    this.personName = personName;
  }

  public Integer getPersonId() {
    return personId;
  }

  public String getPersonName() {
    return personName;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
            .add("personId=" + personId)
            .add("personName=" + personName)
            .toString();
  }
}