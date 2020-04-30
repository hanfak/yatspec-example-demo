package domain;

import java.util.StringJoiner;

public class Person {
  private final String species; // TODO change name
  private final String name;
  private final String birthYear;

  public Person(String species, String name, String birthYear) {
    this.species = species;
    this.name = name;
    this.birthYear = birthYear;
  }

  public String getSpecies() {
    return species;
  }

  public String getName() {
    return name;
  }

  public String getBirthYear() {
    return birthYear;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
            .add("species='" + species + "'")
            .add("name='" + name + "'")
            .add("birthYear='" + birthYear + "'")
            .toString();
  }
}
