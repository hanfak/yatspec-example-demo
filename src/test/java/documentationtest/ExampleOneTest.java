package documentationtest;

import com.googlecode.yatspec.junit.SpecRunner;
import databaseservice.CharacterDataProvider;
import domain.Person;
import domain.Species;
import fileservice.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import starwarsservice.StarWarsService;
import webserver.UseCaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpecRunner.class)
public class ExampleOneTest {

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
  }

  private void andStarWarsServiceReturnsTheCharacterInformation() throws IOException {
    speciesAdd = "www.addd.com";
    yoda = new Person(speciesAdd, "Yoda", "19 BBY");
    when(starWarsService.getCharacterInfo("22")).thenReturn(yoda); // TODO use argument captor
  }

  private void andStarWarsServiceReturnsTheSpeciesInformation() throws IOException {
    species = new Species("species", 999, 1.3);
    when(starWarsService.getSpeciesInfo(speciesAdd)).thenReturn(species);
  }

  private void whenTheUsecaseServiceIsCalledWithId(String id) throws IOException {
    when(request.getPathInfo()).thenReturn("/Yoda");
    when(response.getWriter()).thenReturn(printWriter);
    underTest.doGet(request, response);
  }

  private void thenTheResponseHasStatusCode(int statusCode) {
    verify(response).setStatus(statusCode);
  }

  private void andTheResponseHasABodyOf(String body) {
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
}
