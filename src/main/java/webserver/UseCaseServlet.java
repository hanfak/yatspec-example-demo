package webserver;

import databaseservice.DataProvider;
import domain.Person;
import domain.Species;
import fileservice.FileService;
import starwarsservice.StarWarsService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseServlet extends HttpServlet {

  private final StarWarsService starWarsService;
  private final DataProvider dataProvider;
  private final FileService fileService;

  public UseCaseServlet(StarWarsService starWarsService, DataProvider dataProvider, FileService fileService) {
    this.starWarsService = starWarsService;
    this.dataProvider = dataProvider;
    this.fileService = fileService;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get param from url
    String personName = request.getPathInfo().substring(1);
    // Get data from file/database (either different end point)
    // - Get character id using name
    String personId = String.valueOf(dataProvider.getPersonId(personName));

    // Go to third party app get data
    Person characterInfo = starWarsService.getCharacterInfo(personId);

    // Store characterInfo data in database
    dataProvider.storeCharacterInfo(personId, characterInfo);

    // Send aync message ie jms
    // https://stackoverflow.com/questions/3970567/mock-or-simulate-message-queue-jms

    // Go to another third party app to get data
    Species speciesInfo = starWarsService.getSpeciesInfo(characterInfo.getSpecies());

    // Store data in file
    fileService.storeData(personId, characterInfo, speciesInfo);

    // Add some logs ???

    // Outgoing Response
    response.getWriter().print(format("{\"Description\": \"%s is a %s is born on %s\"}",
            characterInfo.getName(), speciesInfo.getName(), characterInfo.getBirthYear()));
    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
  }
}
// database - use test container
// file service - use acutally files or mocks
// http service - use wiremock
// jms - mockito/stub or lbrary

// TODO do post, which takes json body, and insert data to Character table

// TOOD do delete, delete all tables data