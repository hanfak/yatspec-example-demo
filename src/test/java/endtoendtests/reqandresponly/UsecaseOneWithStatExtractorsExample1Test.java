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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;


@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseOneWithStatExtractorsExample1Test extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    when(weMakeAGetRequestTo("/usecaseone"));

    then(the(statusCode()), is(200));
    and(responseBody(), containsText("Hello, World"));
    and(responseHeaders(), hasHeaderValue("text/html"));
  }

  private ActionUnderTest weMakeAGetRequestTo(String path) {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, path);
  }

  @SuppressWarnings("rawtypes") //For test, in control
  public StateExtractor<Integer> statusCode() {
    return capturedInputAndOutputs -> ((HttpResponse)capturedTestState.get("response")).getStatus();
  }

  // Wrappers to make yatspec output readable
  private StateExtractor<Integer> the(StateExtractor<Integer> statusCode) {
    return statusCode;
  }

  public <ItemOfInterest> TestState and(StateExtractor<ItemOfInterest> extractor, Matcher<? super ItemOfInterest> matcher) throws Exception {
    return then(extractor, matcher);
  }

  @SuppressWarnings("rawtypes") //For test, in control
  public StateExtractor<String> responseBody() {
    return capturedInputAndOutputs -> {
      HttpResponse response = (HttpResponse) capturedTestState.get("response");
      return (String) response.getBody();
    };
  }

  @SuppressWarnings("rawtypes") //For test, in control
  public StateExtractor<List<String>> responseHeaders() {
    return capturedInputAndOutputs -> {
      HttpResponse response = (HttpResponse) capturedTestState.get("response");
      return response.getHeaders().values().stream()
              .flatMap(List::stream).collect(Collectors.toList());
    };
  }

  // Wrapped matcher for better readability
  private Matcher<Iterable<? super String>> hasHeaderValue(String value) {
    return hasItem(value);
  }

  private CapturedInputAndOutputs whenWeMakeARequestTo(CapturedInputAndOutputs capturedInputAndOutputs, String path) throws UnirestException {
    capturedInputAndOutputs.add(format("Request from %s to %s", "User", "Application"),
            requestOutput(path, new HashMap<>(), ""));

    HttpResponse<String> response = Unirest.get(HOST + path).asString();

    capturedTestState.add("response", response);
    // can chain add to maps, can access each of them individually ie
    // capturedTestState.add("actualStatus", response.getStatus()).add("actualHeaders", response.getHeaders()).add("actualBody", response.getBody());
    capturedInputAndOutputs.add(format("Response from %s to %s", "Application", "User"),
            responseOutput(response.getStatus(), response.getHeaders(), response.getBody()));
    return capturedInputAndOutputs;
  }

  // Custom matcher, if hamcrest does not have specific matcher, or want wording yatspec report to be readable
  public Matcher<String> containsText(String text) {
    return new TypeSafeMatcher<String>() {
      @Override
      protected boolean matchesSafely(String body) {
        return body.contains(text);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(format("The response body does not contain '%s'", text));
      }
    };
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
    application.start("application/target/classes/application.test.properties");
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


