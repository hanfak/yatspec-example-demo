package documentationtest;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import databaseservice.CharacterDataProvider;
import domain.Person;
import domain.Species;
import fileservice.FileService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import starwarsservice.StarWarsService;
import webserver.UseCaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// Using yatspec output, to log details which are important or useful for
// documentation or for the reader, to give better insight
@RunWith(SpecRunner.class)
public class ExampleTwoTest implements WithTestState, WithCustomResultListeners {

  @Test
  public void useCaseHappyPathTest() throws IOException {
    givenADataProviderReturnsTheCharacterNameForId("Yoda");
    andStarWarsServiceReturnsTheCharacterInformation();
    andStarWarsServiceReturnsTheSpeciesInformation();

    whenTheUsecaseServiceIsCalledWithId("Yoda");

    thenTheResponseHasStatusCode(200);
    andTheResponseHasABodyOf("{\"Description\": \"Yoda is a species is born on 19 BBY\"}");
    andTheDataProviderSavesTheCharacterInformation();
    andTheFileServiceStoresTheInformationAboutTheCharacter();
  }

  private void givenADataProviderReturnsTheCharacterNameForId(String name) {
    personId = 22;
    when(characterDataProvider.getPersonId(name)).thenReturn(personId);
    addToGivens(format("person Id in database for %s", name), personId);
  }

  private void andStarWarsServiceReturnsTheCharacterInformation() throws IOException {
    speciesAdd = "www.addd.com";
    yoda = new Person(speciesAdd, "Yoda", "19 BBY");
    when(starWarsService.getCharacterInfo("22")).thenReturn(yoda); // TODO use argument captor
    addToGivens("species info address", speciesAdd);
    log("Response from star wars service for Character Info", yoda);
  }

  private void andStarWarsServiceReturnsTheSpeciesInformation() throws IOException {
    double averageHeight = 1.3;
    int lifeSpan = 999;
    species = new Species("species", lifeSpan, averageHeight);
    when(starWarsService.getSpeciesInfo(speciesAdd)).thenReturn(species);
    addToGivens("species info life span", lifeSpan);
    addToGivens("species info average height", averageHeight);
    log("Response from star wars service for Species Info", yoda);
  }

  private void whenTheUsecaseServiceIsCalledWithId(String id) throws IOException {
    when(request.getPathInfo()).thenReturn("/Yoda");
    log("Incoming request to", request.getPathInfo());
    when(response.getWriter()).thenReturn(printWriter);
    underTest.doGet(request, response);
  }

  private void thenTheResponseHasStatusCode(int statusCode) {
    log("Response Status code", statusCode);
    verify(response).setStatus(statusCode);
  }

  private void andTheResponseHasABodyOf(String body) {
    log("Response Body", body);
    verify(printWriter).print(body);
  }

  private void andTheDataProviderSavesTheCharacterInformation() {
    verify(characterDataProvider).storeCharacterInfo(String.valueOf(personId), yoda);
  }

  private void andTheFileServiceStoresTheInformationAboutTheCharacter() {
    verify(fileService).storeData(String.valueOf(personId), yoda, species);
  }

  private Person yoda;
  private int personId;
  private Species species;
  private String speciesAdd;

  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final PrintWriter printWriter = mock(PrintWriter.class);

  private final StarWarsService starWarsService = mock(StarWarsService.class);
  private final CharacterDataProvider characterDataProvider = mock(CharacterDataProvider.class);
  private final FileService fileService = mock(FileService.class);
  private final UseCaseServlet underTest = new UseCaseServlet(starWarsService, characterDataProvider, fileService);

  // Using yatspec methods in documentation test to display useful stuff in html
  // can be extracted to parent class
  private final TestState testState = new TestState();

  @Override
  public Iterable<SpecResultListener> getResultListeners() throws Exception {
    List<SpecResultListener> specResultListeners = new ArrayList<>();

    HtmlResultRenderer htmlResultRenderer = new HtmlResultRenderer()
            .withCustomRenderer(String.class, new NewLineAsHtmlBreakRenderer());

    specResultListeners.add(htmlResultRenderer);
    return specResultListeners;
  }

  @Override
  public TestState testState() {
    return testState;
  }

  public class NewLineAsHtmlBreakRenderer implements Renderer<String> {
    @Override
    public String render(String s) throws Exception {
      return s.replaceAll("\\n", "<br/>");
    }
  }

  protected <T> void log(String title, T value) {
    testState.log(title, value);
  }

  protected <T> void log(String title, Collection<?> value) {
    testState.log(title, StringUtils.join(value, "<br/>"));
  }

  public <T> T addToGivens(String key, T t) {
    testState.interestingGivens.add(key, t);
    return t;
  }
}
