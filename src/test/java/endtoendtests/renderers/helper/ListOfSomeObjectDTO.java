package endtoendtests.renderers.helper;

import java.util.Arrays;
import java.util.List;

public class ListOfSomeObjectDTO {
  private final List<SomeObjectDTO> list = Arrays.asList(
          new SomeObjectDTO("Boo", true, 1),
          new SomeObjectDTO("foo", false, 2),
          new SomeObjectDTO("Maa", true, 3),
          new SomeObjectDTO("Zap", false, 4)
  );

  public List<SomeObjectDTO> getAll() {
    return list;
  }
}
