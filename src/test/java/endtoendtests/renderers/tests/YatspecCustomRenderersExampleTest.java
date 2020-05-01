package endtoendtests.renderers.tests;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import endtoendtests.renderers.helper.ListOfSomeObjectDTO;
import endtoendtests.renderers.renderers.ListOfSomeObjectDTOJsonRenderer;
import endtoendtests.renderers.renderers.ListOfSomeObjectDTORenderer;
import endtoendtests.renderers.renderers.LocalDateTimeRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static java.util.Collections.singletonList;

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class YatspecCustomRenderersExampleTest extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() {
    renderLocalDateTime(LocalDateTime.now());
    renderListOfObjects();
  }

  private void renderListOfObjects() {
    capturedInputAndOutputs.add(new ListOfSomeObjectDTO());
  }

  private void renderLocalDateTime(LocalDateTime now) {
    capturedInputAndOutputs.add(now);
  }

  @Override
  public Iterable<SpecResultListener> getResultListeners() {
    return singletonList(new HtmlResultRenderer()
            .withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows())
            .withCustomRenderer(LocalDateTime.class, new LocalDateTimeRenderer())
            .withCustomRenderer(ListOfSomeObjectDTO.class, new ListOfSomeObjectDTORenderer())
            .withCustomRenderer(ListOfSomeObjectDTO.class, new ListOfSomeObjectDTOJsonRenderer())
    );
  }
}


