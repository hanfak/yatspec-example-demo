import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Slf4jRequestLogWriter;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;

import java.util.EnumSet;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zalando.logbook.Conditions.exclude;
import static org.zalando.logbook.Conditions.requestTo;
import static org.zalando.logbook.DefaultHttpLogWriter.Level.INFO;

public class Application {
  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  public static void main(String... args) {
    JettyWebServer jettyWebServer = new JettyWebServer(2222, APPLICATION_LOGGER);
    ServletContextHandler servletContextHandler = new ServletContextHandler();
    addLoggingFilter(servletContextHandler);
    jettyWebServer.withRequestLog(createRequestLog());
    servletContextHandler.addServlet(new ServletHolder(new UseCaseServlet()), "/usecase");
    jettyWebServer.withHandler(servletContextHandler);
    jettyWebServer.startServer();
  }



  public static void addLoggingFilter(ServletContextHandler servletContextHandler) {
    Logbook logbook = Logbook.builder()
            .condition(exclude(requestTo("/ready")))
            .writer(new DefaultHttpLogWriter(getLogger(LoggingCategory.AUDIT.name()), INFO))
            .build();
    FilterHolder filterHolder = new FilterHolder(new LogbookFilter(logbook));
    servletContextHandler.addFilter(filterHolder, "/*", EnumSet.of(REQUEST, ASYNC, ERROR));
  }

  public static CustomRequestLog createRequestLog() {
    Slf4jRequestLogWriter slf4jRequestLogWriter = new Slf4jRequestLogWriter();
    slf4jRequestLogWriter.setLoggerName(LoggingCategory.ACCESS.name());
    String requestLogFormat = CustomRequestLog.EXTENDED_NCSA_FORMAT;
    return new CustomRequestLog(slf4jRequestLogWriter, requestLogFormat);
  }
}
