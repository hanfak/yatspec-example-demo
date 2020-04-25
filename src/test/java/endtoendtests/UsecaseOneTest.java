package endtoendtests;

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
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseOneTest extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    when(weMakeAGetRequestTo("/usecaseone")); // Using yatspec dsl

    thenItReturnsAStatusCodeOf(200);
    andTheBodyIs("Hello, World");
    andTheHeaderContains("text/html");
  }

  private ActionUnderTest weMakeAGetRequestTo(String path) {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, path);
  }

  private void thenItReturnsAStatusCodeOf(int expected) {
    assertThat(statusCode).isEqualTo(expected);
  }

  private void andTheBodyIs(String body) {
    assertThat(responseBody).isEqualTo(body);
  }

  private void andTheHeaderContains(String header) {
    List<String> headerValues = responseHeaders.values().stream()
            .flatMap(List::stream).collect(Collectors.toList());
    assertThat(headerValues).contains(header);
  }

  private CapturedInputAndOutputs whenWeMakeARequestTo(CapturedInputAndOutputs capturedInputAndOutputs, String path) throws UnirestException {
    capturedInputAndOutputs.add(format("Request from %s to %s", "User", "Application"), requestOutput(path, new HashMap<>(), ""));
    HttpResponse<String> httpResponse = Unirest.get(HOST + path).asString(); // The action
    // Storing the results
    responseBody = httpResponse.getBody();
    statusCode = httpResponse.getStatus();
    responseHeaders = httpResponse.getHeaders();
    capturedInputAndOutputs.add(format("Response from %s to %s", "Application", "User"), responseOutput(statusCode, responseHeaders, responseBody));
    return capturedInputAndOutputs;
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

  private String responseBody;
  private Headers responseHeaders;
  private int statusCode;
}
