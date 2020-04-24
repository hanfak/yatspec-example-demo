package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterDetails {
  public final String personId;
  public final String personName;
  public final String birthYear;
  public final Double averageHeight;
  public final Integer lifspan;
  public final String species;

  @JsonCreator
  public CharacterDetails(@JsonProperty("personId") String personId,
                          @JsonProperty("personName") String personName,
                          @JsonProperty("birthYear") String birthYear,
                          @JsonProperty("averageHeight") Double averageHeight,
                          @JsonProperty("lifspan") Integer lifspan,
                          @JsonProperty("species") String species) {
    this.personId = personId;
    this.personName = personName;
    this.birthYear = birthYear;
    this.averageHeight = averageHeight;
    this.lifspan = lifspan;
    this.species = species;
  }
}
