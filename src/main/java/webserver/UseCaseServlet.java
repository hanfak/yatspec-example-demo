package webserver;

import domain.Person;
import domain.Species;
import starwarsservice.StarWarsService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseServlet extends HttpServlet {
  private final StarWarsService starWarsService;

  public UseCaseServlet(StarWarsService starWarsService) {
    this.starWarsService = starWarsService;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String personId = request.getPathInfo().substring(1); // TODO be name of character
    // Get data from file/database (either different end point)
    // - Get character id using name

    // Go to third party app get data
    Person characterInfo = starWarsService.getCharacterInfo(personId);

    // Store characterInfo data in database

    // Send aync message ie jms
    // https://stackoverflow.com/questions/3970567/mock-or-simulate-message-queue-jms

    // Go to another third party app to get data
    Species speciesInfo = starWarsService.getSpeciesInfo(characterInfo.getSpecies());

    // Store data in file

    // Add some logs ???

    // Outgoing Response
    response.getWriter().print(format("{\"Description\": \"%s is a %s is born on %s\"}",
            characterInfo.getName(), speciesInfo.getName(), characterInfo.getBirthYear()));
    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
  }
}
// database - use test container
// http service - use wiremock
// jms - mockito/stub or lbrary