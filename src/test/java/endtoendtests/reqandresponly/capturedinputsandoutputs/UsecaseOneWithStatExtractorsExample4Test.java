package endtoendtests.reqandresponly.capturedinputsandoutputs;

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
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.ListAssert;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wiring.Application;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseOneWithStatExtractorsExample4Test extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    when(weMakeAGetRequestTo("/usecaseone"));

    assertThat(statusCode().execute(capturedInputAndOutputs)).isEqualTo(200);
    thenTheStatusCodeIs(200);
    then(actualStatusCode()).isEqualTo(200);

    assertThat(actualResponseHeaders()).contains("text/html");
    then(actualResponseHeaders()).contains("text/html");
  }

  private ActionUnderTest weMakeAGetRequestTo(String path) {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, path);
  }

  public StateExtractor<Integer> statusCode() {
    return capturedInputAndOutputs -> capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getStatus();
  }

  private void thenTheStatusCodeIs(int expectedStatusCode) throws Exception {
    StateExtractor<Integer> statusCode =
            capturedInputAndOutputs -> capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getStatus();

    assertThat(statusCode.execute(capturedInputAndOutputs)).isEqualTo(expectedStatusCode);
  }

  public StateExtractor<String> responseBody() {
    return capturedInputAndOutputs -> capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getBody();
  }

  public StateExtractor<List<String>> responseHeaders() {
    return capturedInputAndOutputs -> {
      Headers response = capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getHeaders();
      return response.values().stream()
              .flatMap(List::stream).collect(Collectors.toList());
    };
  }

  public List<String> actualResponseHeaders() throws Exception {
    StateExtractor<List<String>> headersStateExtractor = capturedInputAndOutputs -> {
      Headers headers = capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getHeaders();
      return headers.values().stream()
              .flatMap(List::stream).collect(Collectors.toList());
    };
    return headersStateExtractor.execute(capturedInputAndOutputs);
  }

  public <ItemOfInterest> TestState and(StateExtractor<ItemOfInterest> extractor, Matcher<? super ItemOfInterest> matcher) throws Exception {
    return then(extractor, matcher);
  }

  private AbstractIntegerAssert<?> then(Integer actual) {
    return assertThat(actual);
  }

  private ListAssert<String> then(List<String> actual) {
    return assertThat(actual);
  }

  public Integer actualStatusCode() throws Exception {
    StateExtractor<Integer> response = capturedInputAndOutputs -> capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getStatus();
    return response.execute(capturedInputAndOutputs);
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

  private SvgWrapper generateSequenceDiagram() {
    return new SequenceDiagramGenerator()
            .generateSequenceDiagram(new ByNamingConventionMessageProducer().messages(capturedInputAndOutputs));
  }

  @Before
  public void setUp() {
    application.start();
  }

  @After
  public void tearDown() {
    application.stop();
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram()); // creates sequence diagram
  }

  private static final String HOST = "http://localhost:2222";
  private static final String REQUEST_TO_APPLICATION = "Request from User to Pacman";
  private static final String RESPONSE_FROM_APPLICATION= "Response from Pacman to User";

  private final Application application = new Application();
}


