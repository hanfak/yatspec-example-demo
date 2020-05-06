package webserver;

import databaseservice.DataProvider;
import domain.Person;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseSixServlet extends HttpServlet {

  private final StarWarsInterfaceService starWarsInterfaceService;
  private final DataProvider dataProvider;

  public UseCaseSixServlet(StarWarsInterfaceService starWarsInterfaceService, DataProvider dataProvider) {
    this.starWarsInterfaceService = starWarsInterfaceService;
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String personName = request.getPathInfo().substring(1);
    String personId = String.valueOf(dataProvider.getPersonId(personName));

    // Go to third party app get data
    Person characterInfo = starWarsInterfaceService.getCharacterInfo(personId);

    response.getWriter().print(format("{\"Description\": \"%s was born on %s\"}",
            characterInfo.getName(), characterInfo.getBirthYear()));
    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
  }
}
