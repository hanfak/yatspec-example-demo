package starwarsservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class HttpLoggingFormatter {

  String requestOutput(HttpRequest httpRequest) throws IOException {
    return format("%s %s HTTP/1.1%s", httpRequest.getHttpMethod(), httpRequest.getUrl(), "\n")
            + headersFormatter(httpRequest.getHeaders())
            + "\r\n\r\n"
            + requestBody(httpRequest);
  }

  String responseOutput(HttpResponse response) {
    return format("%s %s%n%s%n%n%s", "HTTP", response.getStatus(), headersFormatter(response.getHeaders()), responseBody(response));
  }

  private String requestBody(HttpRequest httpRequest) throws IOException {
    if (httpRequest.getBody() == null) {
      return "";
    }
    return Optional.ofNullable( httpRequest.getBody().getEntity().getContent())
            .map(input -> new Scanner(input, "UTF-8").useDelimiter("\\A"))
            .filter(Scanner::hasNext)
            .map(Scanner::next)
            .orElse("");
  }

  private String responseBody(HttpResponse response) {
    return Optional.ofNullable( response.getBody())
            .map(Object::toString)
            .orElse("");
  }

  private String headersFormatter(Map<String, List<String>> headers) {
    return headers.entrySet().stream()
            .map(header -> format("%s: %s", header.getKey(), String.join(",", header.getValue())))
            .collect(joining(lineSeparator()));
  }
}
