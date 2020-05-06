package wiring;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import databaseservice.CharacterDataProvider;
import fileservice.CounterService;
import fileservice.FileService;
import logging.LoggingCategory;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Slf4jRequestLogWriter;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;
import settings.Settings;
import starwarsservice.HttpLoggingFormatter;
import starwarsservice.LoggingHttpClient;
import starwarsservice.StarWarsService;
import starwarsservice.UnirestHttpClient;
import webserver.*;

import java.util.EnumSet;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;
import static logging.LoggingCategory.ACCESS;
import static org.eclipse.jetty.server.CustomRequestLog.EXTENDED_NCSA_FORMAT;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zalando.logbook.DefaultHttpLogWriter.Level.INFO;
import static settings.PropertyLoader.load;

public class Application {

  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private JettyWebServer jettyWebServer;

  public static void main(String... args) {
    new Application().start("target/classes/application.prod.properties");
  }

  // TODO tidy up use wiriing/factories
  // For test to access
  public void start(String propertyFile) {
    Settings settings = load(propertyFile);
    CharacterDataProvider characterDataProvider = new CharacterDataProvider();
    LoggingHttpClient httpClient = new LoggingHttpClient(new UnirestHttpClient(), new HttpLoggingFormatter());
    StarWarsService starWarsService = new StarWarsService(httpClient, settings);
    FileService fileService = new FileService(new CounterService(), new XmlMapper());
    UseCaseServlet useCaseServlet = new UseCaseServlet(starWarsService, characterDataProvider, fileService);
    UseCaseOneServlet useCaseOneServlet = new UseCaseOneServlet();
    UseCaseTwoServlet useCaseTwoServlet = new UseCaseTwoServlet(characterDataProvider);
    UseCaseThreeServlet useCaseThreeServlet = new UseCaseThreeServlet(characterDataProvider);
    UseCaseFourServlet useCaseFourServlet = new UseCaseFourServlet(characterDataProvider);
    UseCaseFiveServlet useCaseFiveServlet = new UseCaseFiveServlet(characterDataProvider);
    UseCaseSixServlet useCaseSixServlet = new UseCaseSixServlet(starWarsService, characterDataProvider);
    UseCaseSevenServlet useCaseSevenServlet = new UseCaseSevenServlet(starWarsService, characterDataProvider);

    ServletContextHandler servletContextHandler = createWebserver();
    addServlets(servletContextHandler, useCaseServlet, useCaseOneServlet, useCaseTwoServlet, useCaseThreeServlet, useCaseFourServlet, useCaseFiveServlet, useCaseSixServlet, useCaseSevenServlet);
    jettyWebServer.withHandler(servletContextHandler);

    jettyWebServer.startServer();
  }

  private void addServlets(ServletContextHandler servletContextHandler, UseCaseServlet useCaseServlet, UseCaseOneServlet useCaseOneServlet, UseCaseTwoServlet useCaseTwoServlet, UseCaseThreeServlet useCaseThreeServlet, UseCaseFourServlet useCaseFourServlet, UseCaseFiveServlet useCaseFiveServlet, UseCaseSixServlet useCaseSixServlet, UseCaseSevenServlet useCaseSevenServlet) {
    servletContextHandler.addServlet(new ServletHolder(useCaseServlet), "/usecase/*");
    servletContextHandler.addServlet(new ServletHolder(useCaseOneServlet), "/usecaseone");
    servletContextHandler.addServlet(new ServletHolder(useCaseTwoServlet), "/usecasetwo");
    servletContextHandler.addServlet(new ServletHolder(useCaseThreeServlet), "/usecasethree/*");
    servletContextHandler.addServlet(new ServletHolder(useCaseFourServlet), "/usecasefour/*");
    servletContextHandler.addServlet(new ServletHolder(useCaseFiveServlet), "/usecasefive/*");
    servletContextHandler.addServlet(new ServletHolder(useCaseSixServlet), "/usecasesix/*");
    servletContextHandler.addServlet(new ServletHolder(useCaseSevenServlet), "/usecaseseven/*");
  }

  private ServletContextHandler createWebserver() {
    jettyWebServer = new JettyWebServer(2222, APPLICATION_LOGGER);
    jettyWebServer.withRequestLog(createRequestLog());
    ServletContextHandler servletContextHandler = new ServletContextHandler();
    addLoggingFilter(servletContextHandler);
    return servletContextHandler;
  }

  public static void addLoggingFilter(ServletContextHandler servletContextHandler) {
    Logbook logbook = Logbook.builder()
            .writer(new DefaultHttpLogWriter(getLogger(LoggingCategory.AUDIT.name()), INFO))
            .build();
    FilterHolder filterHolder = new FilterHolder(new LogbookFilter(logbook));
    servletContextHandler.addFilter(filterHolder, "/*", EnumSet.of(REQUEST, ASYNC, ERROR));
  }

  public static CustomRequestLog createRequestLog() {
    Slf4jRequestLogWriter slf4jRequestLogWriter = new Slf4jRequestLogWriter();
    slf4jRequestLogWriter.setLoggerName(ACCESS.name());
    return new CustomRequestLog(slf4jRequestLogWriter, EXTENDED_NCSA_FORMAT);
  }

  // For testing
  public void stop() {
    jettyWebServer.stopServer();
  }
}
