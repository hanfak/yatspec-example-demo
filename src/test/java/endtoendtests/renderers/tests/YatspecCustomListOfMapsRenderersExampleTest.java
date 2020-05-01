package endtoendtests.renderers.tests;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import endtoendtests.renderers.helper.ListOfMapsToRender;
import endtoendtests.renderers.helper.SomeObjectDTO;
import endtoendtests.renderers.renderers.ListOfMapsRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class YatspecCustomListOfMapsRenderersExampleTest extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() {
    renderListOfMaps();
  }

  private void renderListOfMaps() {
    Map<String, Object> map = new LinkedHashMap<>();
    Map<String, Object> map1 = new LinkedHashMap<>();
    Map<String, Object> map2 = new LinkedHashMap<>();
    map.put("First", new SomeObjectDTO("Boo", true, 1));
    map.put("Second", new SomeObjectDTO("Boo1", true, 12));
    map1.put("First", new SomeObjectDTO("Maa", true, 3));
    map1.put("Second", new SomeObjectDTO("Maa1", false, 2));
    map2.put("First", new SomeObjectDTO("Zap", false, 5));
    map2.put("Second", new SomeObjectDTO("Zap1", false, 32));
    log("Some list of maps", Arrays.asList(map, map1, map2));
  }

  protected void log(String title, List<Map<String, Object>> value) {
    log(title, new ListOfMapsToRender(value));
  }

  @Override
  public Iterable<SpecResultListener> getResultListeners() {
    return singletonList(new HtmlResultRenderer()
            .withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows())
            .withCustomRenderer(ListOfMapsToRender.class, new ListOfMapsRenderer())
    );
  }
}


