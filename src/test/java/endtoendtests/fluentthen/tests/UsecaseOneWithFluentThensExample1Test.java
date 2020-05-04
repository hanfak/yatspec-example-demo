package endtoendtests.fluentthen.tests;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import endtoendtests.fluentthen.thens.Then;
import endtoendtests.helper.UnirestRequestWrapper;
import endtoendtests.helper.UnirestResponseWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wiring.Application;

import static endtoendtests.fluentthen.thens.CapturedInputAndOutputKeys.REQUEST_TO_APPLICATION;
import static endtoendtests.fluentthen.thens.CapturedInputAndOutputKeys.RESPONSE_FROM_APPLICATION;
import static java.util.Collections.singletonList;
/**
 * Using a fluent interface for the thens (and when/givens) means less wrapper methods,
 * easier to control contents
*/
@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseOneWithFluentThensExample1Test implements WithTestState, WithCustomResultListeners {

  // Different ways of using them
  @Test
  public void shouldReturnResponse() throws Exception {
    when(weMakeAGetRequestTo("/usecaseone"));

    then.theResponse().hasStatusCodeOf(200);
    and.theResponse().hasBodyOf("Hello, World");
    and.theResponse().hasContentTypeContaining("text/html");

    then.theResponse()
            .isSuccessful()
            .hasHeader("Content-Type").withHeaderValue("text/html")
            .hasHeader("Content-Type").hasHeaderValueContaining("text/html")
            .hasHeader("Content-Type").hasHeaderValueStartingWith("text/html")
            .hasHeader("Content-Type").isTexthtml()
            .hasHeader("Tracey-id")
            .hasHeaderThatMatches(TRACEY_ID_REGEX)
                .and().containsOnlyAlphaNumericAndDashCharacters()
            .and().hasBodyOf("Hello, World").bodyContains(", Wo")
            .and().hasContentTypeContaining("text/html");

    then.theResponse().hasHeader("Tracey-id")
            .hasHeaderThatMatches(TRACEY_ID_REGEX)
            .and().containsOnlyAlphaNumericAndDashCharacters();
  }

  private void when(ActionUnderTest actionUnderTest) throws Exception {
    interactions.capturedInputAndOutputs = actionUnderTest.execute(interactions.interestingGivens, interactions.capturedInputAndOutputs);
  }

  private ActionUnderTest weMakeAGetRequestTo(String path) {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, path);
  }

  private CapturedInputAndOutputs whenWeMakeARequestTo(CapturedInputAndOutputs capturedInputAndOutputs, String path) throws UnirestException {
    HttpRequest getRequest = Unirest.get(HOST + path);
    UnirestRequestWrapper requestWrapper = new UnirestRequestWrapper(getRequest);
    capturedInputAndOutputs.add(REQUEST_TO_APPLICATION, requestWrapper);
    UnirestResponseWrapper response = new UnirestResponseWrapper(getRequest.asString());
    capturedInputAndOutputs.add(RESPONSE_FROM_APPLICATION, response);
    return capturedInputAndOutputs;
  }

  @Override
  public Iterable<SpecResultListener> getResultListeners() {
    return singletonList(new HtmlResultRenderer()
            .withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows())
            .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()));
  }

  @Override
  public TestState testState() {
    return interactions;
  }

  private SvgWrapper generateSequenceDiagram() {
    return new SequenceDiagramGenerator()
            .generateSequenceDiagram(new ByNamingConventionMessageProducer().messages(interactions.capturedInputAndOutputs));
  }

  @Before
  public void setUp() {
    application.start("application/target/classes/application.test.properties");
  }

  @After
  public void tearDown() {
    application.stop();
    interactions.capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram()); // creates sequence diagram
  }
  private static final String HOST = "http://localhost:2222";
  private static final String TRACEY_ID_REGEX = "\\p{Alnum}{8}-\\p{Alnum}{4}-\\p{Alnum}{4}-\\p{Alnum}{4}-\\p{Alnum}{12}";

  private final TestState interactions = new TestState();
  private final Then then = new Then(testState());
  private final Then and = then;

  private final Application application = new Application();
}


