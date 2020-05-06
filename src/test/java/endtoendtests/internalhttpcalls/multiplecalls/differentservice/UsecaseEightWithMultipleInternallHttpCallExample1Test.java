package endtoendtests.internalhttpcalls.multiplecalls.differentservice;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.*;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import endtoendtests.database.TestDataProvider;
import endtoendtests.helper.UnirestRequestWrapper;
import endtoendtests.helper.UnirestResponseWrapper;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens.starwarsservice.GivenStarWarsServiceCharacterInfoSearch;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens.starwarsservice.GivenStarWarsServiceSpeciesSearch;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock.WiremockSetup;
import endtoendtests.internalhttpcalls.stub.ByCustomNamingConventionMessageProducer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import wiring.Application;

import java.util.List;
import java.util.Random;

import static com.googlecode.yatspec.state.givenwhenthen.SyntacticSugar.andA;
import static endtoendtests.helper.CapturedInputAndOutputKeys.REQUEST_TO_APPLICATION;
import static endtoendtests.helper.CapturedInputAndOutputKeys.RESPONSE_FROM_APPLICATION;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 */
@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseEightWithMultipleInternallHttpCallExample1Test extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    given(theCharacterTableIsPrimedWith(PERSON_ID, andA(PERSON_NAME)));
    and(starWarsServiceCharacterInfoSearch().withBirthYear(BIRTH_YEAR).withSpeciesLink(SPECIES_LINK));
    and(starWarsServiceSpeciesSearch().withSpeciesName(SPECIES_NAME));

    when(weMakeAGetRequest());

    then(responseBody(), is(EXPECTED_RESPONSE_BODY));
  }

  private GivensBuilder theCharacterTableIsPrimedWith(Integer personId, String personName) {
    return interestingGivens -> {
      testDataProvider.storeCharacter(personId, personName);
      return interestingGivens.add("Person Id", personId).add("Person Name", personName);
    };
  }

  private GivenStarWarsServiceCharacterInfoSearch starWarsServiceCharacterInfoSearch() {
    return starWarsServiceCharacterInfoSearch.willReturn().withPersonId(PERSON_ID).withPersonName(PERSON_NAME);
  }

  private GivenStarWarsServiceSpeciesSearch starWarsServiceSpeciesSearch() {
    return starWarsServiceSpeciesSearch.withSpeciesLink(SPECIES_LINK);
  }

  private ActionUnderTest weMakeAGetRequest() {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, "/usecaseeight/" + PERSON_NAME);
  }

  private CapturedInputAndOutputs whenWeMakeARequestTo(CapturedInputAndOutputs capturedInputAndOutputs, String path) throws UnirestException {
    HttpRequest getRequest = Unirest.get(HOST + path);
    UnirestRequestWrapper requestWrapper = new UnirestRequestWrapper(getRequest);
    capturedInputAndOutputs.add(REQUEST_TO_APPLICATION, requestWrapper);
    UnirestResponseWrapper response = new UnirestResponseWrapper(getRequest.asString());
    capturedInputAndOutputs.add(RESPONSE_FROM_APPLICATION, response);
    return capturedInputAndOutputs;
  }

  private StateExtractor<String> responseBody() {
    return capturedInputAndOutputs -> capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class).getBody();
  }

  @Override
  public Iterable<SpecResultListener> getResultListeners() {
    return singletonList(new HtmlResultRenderer()
            .withCustomHeaderContent(com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator.getHeaderContentForModalWindows())
            .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()));
  }

  private SvgWrapper generateSequenceDiagram() {
    return new SequenceDiagramGenerator()
            .generateSequenceDiagram(new ByCustomNamingConventionMessageProducer().messages(capturedInputAndOutputs));
  }

  private <T> T addToGivens(String key, T t) {
    testState().interestingGivens.add(key, t);
    return t;
  }

  @Before
  public void setUp() {
    wiremockSetup.listenToWiremock();
    wiremockSetup.configureDefaultMappings();
    testDataProvider.deleteAllInfoFromAllTables();
    application.start("target/test-classes/application.test.properties");
  }

  @Rule
  public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @After
  public void tearDown() {
    testState().capturedInputAndOutputs.add("Wiremock Primings from /__admin/", wiremockSetup.wiremockPrimings());
    application.stop();
    wiremockSetup.resetWireMockMappings();
    wiremockSetup.stopWiremockServer();
    capturedInputAndOutputs.add("Logs", systemOutRule.getLog());
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram());

    // TODO does unirest need to be stopped??
  }

  private static final Integer PERSON_ID = new Random().ints(100, (999)).findFirst().orElse(0);
  private static final List<String> randomNames = asList("Loial", "Rand", "Erith", "Mat", "Bobo");
  private static final String PERSON_NAME = randomNames.get(new Random().nextInt(randomNames.size()));
  private final String BIRTH_YEAR = addToGivens("Birth Year", "19BBY");
  private final String SPECIES_NAME = addToGivens("Species Name", "Hobbit");
  private final String SPECIES_LINK = addToGivens("Species Link", "http://localhost:8888/starwars/species/5");
  private static final String HOST = "http://localhost:2222";
  private final String EXPECTED_RESPONSE_BODY = format("{\"Description\": \"%s Desilijic Tiure was born on %s and is Hobbit species\"}", PERSON_NAME, BIRTH_YEAR);

  private final WiremockSetup wiremockSetup = new WiremockSetup(testState());
  private final GivenStarWarsServiceCharacterInfoSearch starWarsServiceCharacterInfoSearch = new GivenStarWarsServiceCharacterInfoSearch(wiremockSetup);
  private final GivenStarWarsServiceSpeciesSearch starWarsServiceSpeciesSearch = new GivenStarWarsServiceSpeciesSearch(wiremockSetup);
  private final TestDataProvider testDataProvider = new TestDataProvider();

  private final Application application = new Application();
}
