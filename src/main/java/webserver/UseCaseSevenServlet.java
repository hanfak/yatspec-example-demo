package webserver;

import databaseservice.DataProvider;
import domain.Person;
import domain.Species;
import starwarsservice.StarWarsService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseSevenServlet extends HttpServlet {

  private final StarWarsService starWarsService;
  private final DataProvider dataProvider;

  public UseCaseSevenServlet(StarWarsService starWarsService, DataProvider dataProvider) {
    this.starWarsService = starWarsService;
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String personName = request.getPathInfo().substring(1);
    String personId = String.valueOf(dataProvider.getPersonId(personName));

    // Go to third party app get data
    Person characterInfo = starWarsService.getCharacterInfo(personId);
    Species speciesInfo = starWarsService.getSpeciesInfo(characterInfo.getSpecies());

    response.getWriter().print(format("{\"Description\": \"%s was born on %s and is %s species\"}",
            characterInfo.getName(), characterInfo.getBirthYear(), speciesInfo.getName()));
    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
  }
}
