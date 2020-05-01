package endtoendtests.database;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.*;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import endtoendtests.helper.UnirestRequestWrapper;
import endtoendtests.helper.UnirestResponseWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wiring.Application;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseTwoWithDatabaseExample2Test extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    given(theSpecifiesTableIsPrimedWith(1, speciesName("Ogier"), averageHeight(3.5F), andLifespan(500)));

    when(weMakeAGetRequestTo("/usecasetwo"));

    then(responseBody(), is("Hello, Ogier, who lives for 500 years and has average height of 3.5 metres"));
  }

  private GivensBuilder theSpecifiesTableIsPrimedWith(int personId, String name, float avgHeight, int lifeSpan) {
    // uses a givens builder to run the priming of the database
    return interestingGivens -> {
      testDataProvider.storeSpecifiesInfo(personId, name, avgHeight, lifeSpan);
      // Add this to yatspec output, and highlights the values in the tests
      interestingGivens.add("personId", personId);
      interestingGivens.add("species name", name);
      interestingGivens.add("average height", avgHeight);
      interestingGivens.add("lifespan", lifeSpan);
      return interestingGivens;
    };
  }

  // Wrappers to make yatspec out readable
  private String speciesName(String name) {
    return name;
  }

  private float averageHeight(float avgHeight) {
    return avgHeight;
  }

  private int andLifespan(int lifespan) {
    return lifespan;
  }

  private ActionUnderTest weMakeAGetRequestTo(String path) {
    return (interestingGivens, capturedInputAndOutputs) -> {
      // can access interesting givens in when:
      Integer lifespan = interestingGivens.getType("lifespan", Integer.class);
      System.out.println("lifespan = " + lifespan);
      return whenWeMakeARequestTo(capturedInputAndOutputs, path);
    };
  }

  public StateExtractor<String> responseBody() {
    return capturedInputAndOutputs -> capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getBody();
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
    testDataProvider.deleteAllInfoFromAllTables();
    testDataProvider.populateTables();
    application.start();
  }

  @After
  public void tearDown() {
    application.stop();
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram()); // creates sequence diagram
  }

  private static final String HOST = "http://localhost:2222";
  private static final String REQUEST_TO_APPLICATION = "Request from User to Pacman";
  private static final String RESPONSE_FROM_APPLICATION = "Response from Pacman to User";
  private final TestDataProvider testDataProvider = new TestDataProvider();
  private final Application application = new Application();
}


