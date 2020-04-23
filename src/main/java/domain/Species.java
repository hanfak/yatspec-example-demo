package domain;

public class Species {
  private final String name;
  private final Integer lifeSpan;
  private final Double averageHeight;

  public Species(String name, Integer lifeSpan, Double averageHeight) {
    this.name = name;
    this.lifeSpan = lifeSpan;
    this.averageHeight = averageHeight;
  }

  public String getName() {
    return name;
  }

  public Integer getLifeSpan() {
    return lifeSpan;
  }

  public Double getAverageHeight() {
    return averageHeight;
  }
}
