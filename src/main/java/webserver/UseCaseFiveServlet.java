package webserver;

import databaseservice.DataProvider;
import domain.Person;
import domain.SpeciesInfo;
import logging.LoggingCategory;
import org.slf4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class UseCaseFiveServlet extends HttpServlet {
  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private final DataProvider dataProvider;

  public UseCaseFiveServlet(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String personName = request.getPathInfo().substring(1);

    // Getting data from db
    Integer personId = dataProvider.getPersonId(personName);
    SpeciesInfo speciesInfo = dataProvider.getSpeciesInfo(personId);

    dataProvider.storeCharacterInfo(Integer.toString(personId), new Person(speciesInfo.species, personName, "19BBY"));

    // Outgoing Response
    response.getWriter().print(format("Hello, %s, who lives for %s years and has average height of %s metres and is born in 19BBY",
            personName,
            speciesInfo.lifeSpan,
            speciesInfo.avgHeight));
    response.setHeader("Content-Type", "text/html");
    response.setStatus(200);
  }
}
