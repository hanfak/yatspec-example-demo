package documentationtest;

import com.googlecode.yatspec.junit.SpecRunner;
import databaseservice.CharacterDataProvider;
import domain.Person;
import domain.Species;
import fileservice.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import thirdparty.starwarsservice.StarWarsService;
import webserver.StarWarsInterfaceService;
import webserver.UseCaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// Also known as acceptance test or business usecase test
// Where the logic of the business is stored
// As it relates to business logic/processes, it is good to document this and output in human readable form
// The is a unit test, so can do lots of these. Useful for documenting lots of different cases,
// instead of writing expensive end to end to tests
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
    when(starWarsInterfaceService.getCharacterInfo("22")).thenReturn(yoda); // TODO use argument captor
  }

  private void andStarWarsServiceReturnsTheSpeciesInformation() throws IOException {
    species = new Species("species", 999, 1.3);
    when(starWarsInterfaceService.getSpeciesInfo(speciesAdd)).thenReturn(species);
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

  private final StarWarsInterfaceService starWarsInterfaceService = mock(StarWarsService.class);
  private final CharacterDataProvider characterDataProvider = mock(CharacterDataProvider.class);
  private final FileService fileService = mock(FileService.class);
  private final UseCaseServlet underTest = new UseCaseServlet(starWarsInterfaceService, characterDataProvider, fileService);
}
