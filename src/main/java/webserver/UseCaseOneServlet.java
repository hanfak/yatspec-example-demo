package webserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UseCaseOneServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Outgoing Response
    response.getWriter().print("Hello, World");
    response.setHeader("Content-Type", "text/html");
    response.setHeader("Tracey-id", UUID.randomUUID().toString());
    response.setStatus(200);
  }
}
