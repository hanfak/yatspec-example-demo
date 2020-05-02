package webserver;

import databaseservice.DataProvider;
import domain.SpeciesInfo;
import logging.LoggingCategory;
import org.slf4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class UseCaseFourServlet extends HttpServlet {
  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private final DataProvider dataProvider;

  public UseCaseFourServlet(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    APPLICATION_LOGGER.info("Retrieving param from url");
    String personName = request.getPathInfo().substring(1);
    APPLICATION_LOGGER.info("Retrieved param from url: " + personName);

    // Getting data from db
    APPLICATION_LOGGER.info("Getting info from dataProvider");
    Integer personId = dataProvider.getPersonId(personName);
    APPLICATION_LOGGER.info("Retrieved personId from dataProvider");
    APPLICATION_LOGGER.info("Getting info from dataProvider");
    SpeciesInfo speciesInfo = dataProvider.getSpeciesInfo(personId);
    APPLICATION_LOGGER.info("Retrieved speciesInfo from dataProvider");
    // Outgoing Response
    response.getWriter().print(format("Hello, %s, who lives for %s years and has average height of %s metres",
            personName,
            speciesInfo.lifeSpan,
            speciesInfo.avgHeight));
    response.setHeader("Content-Type", "text/html");
    response.setStatus(200);
  }
}
