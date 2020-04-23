package webserver;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;

import static java.lang.String.format;

public class JettyWebServer {

    private Server server;
    private final Logger logger;

    public JettyWebServer(int port, Logger logger) {
        this.server = new Server(port);
        this.logger = logger;
    }

    public void startServer() {
        try {
            server.start();
            logger.info("Server started at: " + server.getURI().toString());
        } catch (Exception e) {
            // Unit tests are testing this, but not being picked up with jacoco
            String message = format("Could not start server on port '%d'", server.getURI().getPort());
            logger.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    public void stopServer() {
        try {
            server.stop();
            logger.info("Server stopped");
        } catch (Exception e) {
            // Unit tests are testing this, but not being picked up with jacoco
            String message = format("Could not stop server on port '%d'", server.getURI().getPort());
            logger.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    public JettyWebServer withHandler(Handler handler) {
        server.setHandler(handler);
        return this;
    }

    public JettyWebServer withRequestLog(CustomRequestLog requestLog) {
        server.setRequestLog(requestLog);
        return this;
    }
}
