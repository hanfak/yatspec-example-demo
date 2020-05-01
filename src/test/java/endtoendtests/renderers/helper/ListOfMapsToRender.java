package endtoendtests.renderers.helper;

import java.util.List;
import java.util.Map;

public class ListOfMapsToRender {
  private final List<Map<String, Object>> result;

  public ListOfMapsToRender(final List<Map<String, Object>> result) {
    this.result = result;
  }

  public List<Map<String, Object>> getResult() {
    return result;
  }
}
