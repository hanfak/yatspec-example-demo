package domain;

public class SpeciesInfo {

  public final String species;
  public final Integer lifeSpan;
  public final Float avgHeight;

  public SpeciesInfo(String species, Integer lifeSpan, Float avgHeight) {
    this.species = species;
    this.lifeSpan = lifeSpan;
    this.avgHeight = avgHeight;
  }
}
