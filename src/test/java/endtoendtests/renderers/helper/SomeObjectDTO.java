package endtoendtests.renderers.helper;

import java.util.StringJoiner;

public class SomeObjectDTO {
  private final String string;
  private final boolean bool;
  private final int num;

  public SomeObjectDTO(String string, boolean bool, int num) {
    this.string = string;
    this.bool = bool;
    this.num = num;
  }

  public String getString() {
    return string;
  }

  public boolean isBool() {
    return bool;
  }

  public int getNum() {
    return num;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", SomeObjectDTO.class.getSimpleName() + "[", "]")
            .add("string='" + string + "'")
            .add("bool=" + bool)
            .add("num=" + num)
            .toString();
  }
}
