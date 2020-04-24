package learning.fileservice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.beans.ConstructorProperties;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ExampleObject {

  private final int randomInteger;
  private final double randomFloat;
  private final boolean randomBoolean;
  private final List<Dictionary> dictionary;

  @ConstructorProperties({"randomInteger", "randomFloat", "randomBoolean", "dictionary"})
  public ExampleObject(int randomInteger, double randomFloat, boolean randomBoolean, List<Dictionary> dictionary) {
    this.randomInteger = randomInteger;
    this.randomFloat = randomFloat;
    this.randomBoolean = randomBoolean;
    this.dictionary = dictionary;
  }

  public int getRandomInteger() {
    return randomInteger;
  }

  public double getRandomFloat() {
    return randomFloat;
  }

  public boolean isRandomBoolean() {
    return randomBoolean;
  }

  public List<Dictionary> getDictionary() {
    return dictionary;
  }
}
