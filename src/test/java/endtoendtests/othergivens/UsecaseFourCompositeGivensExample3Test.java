package endtoendtests.othergivens;

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
import endtoendtests.database.TestDataProvider;
import endtoendtests.helper.Person;
import endtoendtests.helper.UnirestRequestWrapper;
import endtoendtests.helper.UnirestResponseWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wiring.Application;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;

@SuppressWarnings("SameParameterValue") // For test readability
/**
 * Use of composite givens, as part of yatspec api
 */
@RunWith(SpecRunner.class)
public class UsecaseFourCompositeGivensExample3Test extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    given(aCharacterAndItsSpeciesIsPrimed());

    when(weMakeAGetRequest());

    then(responseBody(), is(EXPECTED_RESPONSE_BODY));
  }

  private GivensBuilder aCharacterAndItsSpeciesIsPrimed() {
    return CompositeGivens.given(
            theCharacterTableIsPrimed(),
            theSpecifiesTableIsPrimedWith(
            ));
  }

  private TheCharacterTableIsPrimed theCharacterTableIsPrimed() {
    return theCharacterTableIsPrimed
            .withPersonId(PERSON_ID)
            .withCharacterName(PERSON_NAME);
  }

  private GivensBuilder theSpecifiesTableIsPrimedWith() {
    return new GivenTheSpecifiesTableIsPrimed(testDataProvider)
            .withSpeciesName(addToGivens("species name", "Ogier"))
            .withAverageHeight(addToGivens("average height", 3.5F))
            .withLifespan(addToGivens("lifespan", 500));
  }

  private ActionUnderTest weMakeAGetRequest() {
    return (interestingGivens, capturedInputAndOutputs) -> {
      String personName = interestingGivens.getType(Person.class).getPersonName();
      return whenWeMakeARequestTo(capturedInputAndOutputs, "/usecasefour/" + personName);
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

  public <T> T addToGivens(String key, T t) {
    interestingGivens.add(key, t);
    return t;
  }

  @Before
  public void setUp() {
    testDataProvider.deleteAllInfoFromAllTables();
    application.start("application/target/classes/application.test.properties");
  }

  @After
  public void tearDown() {
    application.stop();
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram()); // creates sequence diagram
  }

  private static final String HOST = "http://localhost:2222";
  private static final String REQUEST_TO_APPLICATION = "Request from User to Application";
  private static final String RESPONSE_FROM_APPLICATION = "Response from Application to User";
  private static final Integer PERSON_ID = new Random().ints(100, (999)).findFirst().orElse(0);

  private static final List<String> randomNames = asList("Loial", "Rand", "Erith", "Mat", "Bobo");
  private static final String PERSON_NAME = randomNames.get(new Random().nextInt(randomNames.size()));
  private static final String EXPECTED_RESPONSE_BODY = "Hello, " + PERSON_NAME + ", who lives for 500 years and has average height of 3.5 metres";

  private final TestDataProvider testDataProvider = new TestDataProvider();
  private final TheCharacterTableIsPrimed theCharacterTableIsPrimed = new TheCharacterTableIsPrimed(testDataProvider);
  private final Application application = new Application();
}


