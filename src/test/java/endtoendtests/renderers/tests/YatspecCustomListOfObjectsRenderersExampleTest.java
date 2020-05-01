package endtoendtests.renderers.tests;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import endtoendtests.renderers.helper.ListOfSomeObjectDTO;
import endtoendtests.renderers.renderers.ListOfObjectsRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Collections.singletonList;

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class YatspecCustomListOfObjectsRenderersExampleTest extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() {
    renderListOfObjects();
  }

  private void renderListOfObjects() {
    capturedInputAndOutputs.add(new ListOfSomeObjectDTO());
  }


  @Override
  public Iterable<SpecResultListener> getResultListeners() {
    return singletonList(new HtmlResultRenderer()
            .withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows())
            .withCustomRenderer(ListOfSomeObjectDTO.class, new ListOfObjectsRenderer())

    );
  }
}


