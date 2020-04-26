package webserver;

import databaseservice.DataProvider;
import domain.SpeciesInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseTwoServlet extends HttpServlet {
  private final DataProvider dataProvider;

  public UseCaseTwoServlet(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  // TODO add path for id, which will get info from database which is radomised in test
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Getting data from db
    SpeciesInfo speciesInfo = dataProvider.getSpeciesInfo(1);
    // Outgoing Response
    response.getWriter().print(format("Hello, %s, who lives for %s years and has average height of %s metres",
            speciesInfo.species,
            speciesInfo.lifeSpan,
            speciesInfo.avgHeight));
    response.setHeader("Content-Type", "text/html");
    response.setStatus(200);
  }
}
