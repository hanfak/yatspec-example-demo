package endtoendtests.reqandresponly;

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
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.HamcrestCondition;
import org.assertj.core.api.ListAssert;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wiring.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseOneWithStatExtractorsExample2Test extends TestState implements WithCustomResultListeners {

  // All three assertions (Lines 53 - 55) on status code are the same writing differently
  // Would not use assertion on line 53, as not readable in yatspec output
  @Test
  public void shouldReturnResponse() throws Exception {
    when(weMakeAGetRequestTo("/usecaseone"));

    thenThe(statusCode(), is(200));

    assertThat(statusCode().execute(capturedInputAndOutputs)).isEqualTo(200);
    thenTheStatusCodeIs(200);
    then(actualStatusCode()).isEqualTo(200);

    assertThat(actualResponseHeaders()).contains("text/html");
    then(actualResponseHeaders()).contains("text/html");
  }

  private ActionUnderTest weMakeAGetRequestTo(String path) {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, path);
  }

  private CapturedInputAndOutputs whenWeMakeARequestTo(CapturedInputAndOutputs capturedInputAndOutputs, String path) throws UnirestException {
    capturedInputAndOutputs.add(format("Request from %s to %s", "User", "Application"),
            requestOutput(path, new HashMap<>(), ""));

    HttpResponse<String> response = Unirest.get(HOST + path).asString();

    capturedTestState.add("response", response);
    capturedInputAndOutputs.add(format("Response from %s to %s", "Application", "User"),
            responseOutput(response.getStatus(), response.getHeaders(), response.getBody()));
    return capturedInputAndOutputs;
  }

  // Using assertj underneath instead of hamcrest
  public <ItemOfInterest> TestState thenThe(StateExtractor<ItemOfInterest> extractor, Matcher<? super ItemOfInterest> matcher) throws Exception {
    Condition<? super ItemOfInterest> matcherCondition = new HamcrestCondition<>(matcher);
    assertThat(extractor.execute(this.capturedInputAndOutputs)).is(matcherCondition);
    return this;
  }

  @SuppressWarnings("rawtypes") //For test, in control
  public StateExtractor<Integer> statusCode() {
    return capturedInputAndOutputs -> ((HttpResponse)capturedTestState.get("response")).getStatus();
  }

  private void thenTheStatusCodeIs(int expectedStatusCode) throws Exception {
    StateExtractor<Integer> statusCode =
            capturedInputAndOutputs -> ((HttpResponse) capturedTestState.get("response")).getStatus();

    assertThat(statusCode.execute(capturedInputAndOutputs)).isEqualTo(expectedStatusCode);
  }

  // wrapping assertThat with BDD language - not great as not generic could make it generic using wrapper class
  private AbstractIntegerAssert<?> then(Integer actual) {
    return assertThat(actual);
  }

  public Integer actualStatusCode() throws Exception {
    StateExtractor<Integer> response = capturedInputAndOutputs -> ((HttpResponse) capturedTestState.get("response")).getStatus();
    return response.execute(capturedInputAndOutputs);
  }

  private ListAssert<String> then(List<String> actual) {
    return assertThat(actual);
  }

  @SuppressWarnings("rawtypes") //For test, in control
  public List<String> actualResponseHeaders() throws Exception {
    StateExtractor<List<String>> headersStateExtractor = capturedInputAndOutputs -> {
      HttpResponse response = (HttpResponse) capturedTestState.get("response");
      return response.getHeaders().values().stream()
              .flatMap(List::stream).collect(Collectors.toList());
    };
    return headersStateExtractor.execute(capturedInputAndOutputs);
  }

  // nice html output
  public static String requestOutput(String uri, Map<String, String> headers, String body) {
    String formattedHeaders = headers.entrySet().stream()
            .map(s -> format("%s: %s", s.getKey(), s.getValue()))
            .collect(joining(lineSeparator()));
    return format("%s %s HTTP/1.1%s", "POST", uri, "\n") + formattedHeaders + "\r\n\r\n" + body;
  }

  // nice html output
  public static String responseOutput(int responseStatus, Headers responseHeaders, String responseBody) {
    String formattedHeaders = responseHeaders.entrySet().stream()
            .map(s -> format("%s: %s", s.getKey(), String.join(",", s.getValue())))
            .collect(joining(lineSeparator()));
    return format("%s %s%n%s%n%n%s", "HTTP", responseStatus, formattedHeaders, responseBody);
  }

  @Override
  public Iterable<SpecResultListener> getResultListeners() {
    //sequence diagram setup
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

  private final Application application = new Application();
  private final CapturedTestState capturedTestState = new CapturedTestState();

  private static class CapturedTestState {
    private Map<String, Object> dataToBeAssertedOn = new HashMap<>();

    public CapturedTestState add(String key, Object value){
      dataToBeAssertedOn.put(key, value);
      return this;
    }

    public Object get(String key){
      return dataToBeAssertedOn.get(key);
    }
  }
}
