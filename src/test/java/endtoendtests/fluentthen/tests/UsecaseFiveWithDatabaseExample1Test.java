package endtoendtests.fluentthen.tests;

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
import endtoendtests.fluentthen.thens.ThenTheDatabase;
import endtoendtests.helper.CharacterInfo;
import endtoendtests.helper.Person;
import endtoendtests.helper.UnirestRequestWrapper;
import endtoendtests.helper.UnirestResponseWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import wiring.Application;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;

@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseFiveWithDatabaseExample1Test extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    given(theCharacterTableIsPrimedWith(PERSON_ID, andPersonName(PERSON_NAME)));
    and(theSpecifiesTableIsPrimedWith(speciesName("Ogier"), averageHeight(3.5F), andLifespan(500)));

    when(weMakeAGetRequest());

    then(responseBody(), is(EXPECTED_RESPONSE_BODY));
    then.theDatabase().hasBirthYear("19BBY");
    then.theDatabase().hasCharacterInfo(EXPECTED_CHARACTER_INFO);
  }

  private GivensBuilder theCharacterTableIsPrimedWith(Integer personId, String personName) {
    return interestingGivens -> {
      Person person = new Person(personId, personName);
      testDataProvider.storeCharacter(person.getPersonId(), person.getPersonName());
      return interestingGivens.add(person);
    };
  }

  private GivensBuilder theSpecifiesTableIsPrimedWith(String name, float avgHeight, int lifeSpan) {
    return interestingGivens -> {
      testDataProvider.storeSpecifiesInfo(interestingGivens.getType(Person.class).getPersonId(), name, avgHeight, lifeSpan);
      return interestingGivens
              .add("species name", name)
              .add("average height", avgHeight)
              .add("lifespan", lifeSpan);
    };
  }

  private String andPersonName(String personName) {
    return personName;
  }

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
      String personName = interestingGivens.getType(Person.class).getPersonName();
      return whenWeMakeARequestTo(capturedInputAndOutputs, "/usecasefive/" + personName);
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
    application.start("application/target/classes/application.test.properties");
  }

  @Rule
  public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @After
  public void tearDown() {
    application.stop();
    capturedInputAndOutputs.add("Logs", systemOutRule.getLog());
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram()); // creates sequence diagram
  }

  private static final String HOST = "http://localhost:2222";
  private static final String REQUEST_TO_APPLICATION = "Request from User to Application";
  private static final String RESPONSE_FROM_APPLICATION = "Response from Application to User";
  private static final Integer PERSON_ID = new Random().ints(100, (999)).findFirst().orElse(0);

  private static final List<String> randomNames = asList("Loial", "Rand", "Erith", "Mat", "Bobo");
  private static final String PERSON_NAME = randomNames.get(new Random().nextInt(randomNames.size()));
  private static final String EXPECTED_RESPONSE_BODY = "Hello, " + PERSON_NAME + ", who lives for 500 years and has average height of 3.5 metres and is born in 19BBY";
  private static final CharacterInfo EXPECTED_CHARACTER_INFO = new CharacterInfo(PERSON_ID, PERSON_NAME, "19BBY");

  private final TestDataProvider testDataProvider = new TestDataProvider();
  private final ThenTheDatabase then = new ThenTheDatabase(testState(), testDataProvider);
  private final Application application = new Application();
}


