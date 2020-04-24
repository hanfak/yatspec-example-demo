package learning.fileservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Dictionary {

  private final int key;
  private final String value;

  @JsonCreator
  public Dictionary(@JsonProperty("key") int key, @JsonProperty("value") String value) {
    this.key = key;
    this.value = value;
  }

  public int getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
