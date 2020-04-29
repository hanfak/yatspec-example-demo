package endtoendtests.reqandresponly.database;

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
import endtoendtests.reqandresponly.capturedinputsandoutputs.UnirestRequestWrapper;
import endtoendtests.reqandresponly.capturedinputsandoutputs.UnirestResponseWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wiring.Application;

import java.util.Random;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseThreeWithDatabaseExample1Test extends TestState implements WithCustomResultListeners {
  // Multiple givens, as priming two tables, can hide this if not useful for documentation (ie call one within the other given)
  @Test
  public void shouldReturnResponse() throws Exception {
    given(theCharacterTableIsPrimedWith(PERSON_ID, "Loial"));
    given(theSpecifiesTableIsPrimedWith(PERSON_ID, speciesName(OGIER), averageHeight(AVG_HEIGHT), andLifespan(LIFESPAN)));

    when(weMakeAGetRequest());

    then(responseBody(), is("Hello, Ogier, who lives for 500 years and has average height of 3.5 metres"));
  }

  private GivensBuilder theCharacterTableIsPrimedWith(int personId, String name) {
    return interestingGivens -> {
      // Adding to given some random variable which will be used in the db, and also used in the request(the when)
      interestingGivens.add("person id", personId);
      testDataProvider.storeCharacter(personId, name);
      return interestingGivens;
    };
  }

  private GivensBuilder theSpecifiesTableIsPrimedWith(int personId, String name, float avgHeight, int lifeSpan) {
    return interestingGivens -> {
      testDataProvider.storeSpecifiesInfo(personId, name, avgHeight, lifeSpan);
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

  private ActionUnderTest weMakeAGetRequest() {
    return (interestingGivens, capturedInputAndOutputs) -> {
      // Grabbing the value from interesting given, to use in request
      Integer personId = interestingGivens.getType("person id", Integer.class); // TODO use object instead
      return whenWeMakeARequestTo(capturedInputAndOutputs, "/usecasethree/" + personId);
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
  // Use a wrapper to add to givens, so dont need to create variable and then add to given separately
  public <T> T addToGivens(String key, T t) {
    interestingGivens.add(key, t);
    return t;
  }

  @Before
  public void setUp() {
    testDataProvider.deleteAllInfoFromAllTables();
    application.start();
  }

  @After
  public void tearDown() {
    application.stop();
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram()); // creates sequence diagram
  }

  private static final String HOST = "http://localhost:2222";
  // this can be any value, we don't know, but it will be used in different places and needs to be stored in interesting givens
  private static final int PERSON_ID = new Random().ints(100, (999)).findFirst().orElse(0);
  private static final String REQUEST_TO_APPLICATION = "Request from User to Pacman";
  private static final String RESPONSE_FROM_APPLICATION = "Response from Pacman to User";

  private final String OGIER = addToGivens("species name", "Ogier");
  private final float AVG_HEIGHT = addToGivens("average height", 3.5F);
  private final int LIFESPAN = addToGivens("lifespan", 500);
  private final TestDataProvider testDataProvider = new TestDataProvider();
  private final Application application = new Application();
}


