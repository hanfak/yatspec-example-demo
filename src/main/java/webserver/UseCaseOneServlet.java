package webserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UseCaseOneServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // How to handle headers
    if (!request.getHeader("accept").contains("text/html")) {
      response.setStatus(400);
    } else {
      // Outgoing Response
      response.getWriter().print("Hello, World");
      response.setHeader("Content-Type", "text/html");
      response.setStatus(200);
    }
  }
}
