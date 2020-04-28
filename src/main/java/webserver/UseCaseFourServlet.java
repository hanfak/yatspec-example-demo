package webserver;

import databaseservice.DataProvider;
import domain.SpeciesInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseFourServlet extends HttpServlet {
  private final DataProvider dataProvider;

  public UseCaseFourServlet(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String personName = request.getPathInfo().substring(1);
    // Getting data from db
    Integer personId = dataProvider.getPersonId(personName);
    SpeciesInfo speciesInfo = dataProvider.getSpeciesInfo(personId);
    // Outgoing Response
    response.getWriter().print(format("Hello, %s, who lives for %s years and has average height of %s metres",
            personName,
            speciesInfo.lifeSpan,
            speciesInfo.avgHeight));
    response.setHeader("Content-Type", "text/html");
    response.setStatus(200);
  }
}
