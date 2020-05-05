package endtoendtests.internalhttpcalls;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.http.Response;
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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static endtoendtests.helper.CapturedInputAndOutputKeys.REQUEST_FROM_APPLICATION_TO_STAR_WARS_SERVICE;
import static endtoendtests.helper.CapturedInputAndOutputKeys.REQUEST_TO_APPLICATION;
import static endtoendtests.helper.CapturedInputAndOutputKeys.RESPONSE_FROM_APPLICATION;
import static endtoendtests.helper.CapturedInputAndOutputKeys.RESPONSE_FROM_STAR_WARS_SERVICE_TO_APPLICATION;
import static endtoendtests.internalhttpcalls.stub.YatspecFormatters.toYatspecString;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 */
@SuppressWarnings("SameParameterValue") // For test readability
@RunWith(SpecRunner.class)
public class UsecaseSixWithInternallHttpCallExample2Test extends TestState implements WithCustomResultListeners {

  @Test
  public void shouldReturnResponse() throws Exception {
    given(theCharacterTableIsPrimedWith(PERSON_ID, andPersonName(PERSON_NAME)));
    andTheStarWarsServiceReturnsCharacterInfoContainingBirthYear(BIRTH_YEAR);

    when(weMakeAGetRequest());

    then(responseBody(), is(EXPECTED_RESPONSE_BODY));
  }

  private GivensBuilder theCharacterTableIsPrimedWith(Integer personId, String personName) {
    return interestingGivens -> {
      testDataProvider.storeCharacter(personId, personName);
      return interestingGivens.add("Person Id", personId).add("Person Name", personName);
    };
  }

  private void andTheStarWarsServiceReturnsCharacterInfoContainingBirthYear(String birthYear) {
    registerListener();
    String primedResponse = format(PRIMED_RESPONSE_FROM_STARWARS_TEMPLATE, PERSON_NAME, birthYear, PERSON_ID);
    stubGet("/people/" + PERSON_ID,
            addToGivens("Primed Response from Star wars service", primedResponse),
            "application/json");
  }

  // Using Wiremock as the library, which has a easy to use api to program the response from a stub
  private void stubGet(String path, String body, String contentType) {
    wireMockServer.stubFor(get(urlEqualTo(path))
            .willReturn(aResponse()
                    .withHeader("Content-Type", contentType)
                    .withStatus(200)
                    .withBody(body)));
  }

  private String andPersonName(String personName) {
    return personName;
  }

  private ActionUnderTest weMakeAGetRequest() {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, "/usecasesix/" + PERSON_NAME);
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
            .withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows())
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

  private void stopWiremockServer() {
    wireMockServer.stop();
  }

  private void recordTraffic(Request request, Response response) {
    addToCapturedInputsAndOutputs(REQUEST_FROM_APPLICATION_TO_STAR_WARS_SERVICE, toYatspecString(request));
    addToCapturedInputsAndOutputs(RESPONSE_FROM_STAR_WARS_SERVICE_TO_APPLICATION, toYatspecString(response));
  }

  public void addToCapturedInputsAndOutputs(String key, Object capturedStuff) {
    testState().capturedInputAndOutputs.add(key, capturedStuff);
  }

  protected void registerListener() {
    listenToWiremock(this::recordTraffic);
  }

  public void listenToWiremock(RequestListener listener) {
    wireMockServer.addMockServiceRequestListener(listener);
  }

  @Before
  public void setUp() {
    testDataProvider.deleteAllInfoFromAllTables();
    wireMockServer.start();
    application.start("target/test-classes/application.test.properties");
  }

  @Rule
  public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @After
  public void tearDown() {
    application.stop();
    stopWiremockServer();
    capturedInputAndOutputs.add("Logs", systemOutRule.getLog());
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram());
  }

  private static final Integer PERSON_ID = new Random().ints(100, (999)).findFirst().orElse(0);
  private static final List<String> randomNames = asList("Loial", "Rand", "Erith", "Mat", "Bobo");
  private final String BIRTH_YEAR = addToGivens("Birth Year", "19BBY");
  private static final String PERSON_NAME = randomNames.get(new Random().nextInt(randomNames.size()));
  private final String EXPECTED_RESPONSE_BODY = format("{\"Description\": \"%s Desilijic Tiure was born on %s\"}", PERSON_NAME, BIRTH_YEAR);
  private static final String HOST = "http://localhost:2222";

  private final TestDataProvider testDataProvider = new TestDataProvider();
  private final WireMockServer wireMockServer = new WireMockServer(8888);
  private final Application application = new Application();

  private static String PRIMED_RESPONSE_FROM_STARWARS_TEMPLATE = "{\n" +
          "  \"name\": \"%s Desilijic Tiure\",\n" +
          "  \"height\": \"175\",\n" +
          "  \"mass\": \"1,358\",\n" +
          "  \"hair_color\": \"n/a\",\n" +
          "  \"skin_color\": \"green-tan, brown\",\n" +
          "  \"eye_color\": \"orange\",\n" +
          "  \"birth_year\": \"%s\",\n" +
          "  \"gender\": \"hermaphrodite\",\n" +
          "  \"homeworld\": \"http://swapi.py4e.com/api/planets/24/\",\n" +
          "  \"films\": [\n" +
          "    \"http://swapi.py4e.com/api/films/1/\",\n" +
          "    \"http://swapi.py4e.com/api/films/3/\",\n" +
          "    \"http://swapi.py4e.com/api/films/4/\"\n" +
          "  ],\n" +
          "  \"species\": [\n" +
          "    \"http://swapi.py4e.com/api/species/5/\"\n" +
          "  ],\n" +
          "  \"vehicles\": [ ],\n" +
          "  \"starships\": [ ],\n" +
          "  \"created\": \"2014-12-10T17:11:31.638000Z\",\n" +
          "  \"edited\": \"2014-12-20T21:17:50.338000Z\",\n" +
          "  \"url\": \"http://swapi.py4e.com/api/people/%s/\"\n" +
          "}";
}
