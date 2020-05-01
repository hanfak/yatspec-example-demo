package endtoendtests.helper;

import com.mashape.unirest.request.HttpRequest;

import java.util.Optional;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class UnirestRequestWrapper {

  private final HttpRequest httpRequest;

  public UnirestRequestWrapper(HttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  @Override
  public String toString() {
    String formattedHeaders = httpRequest.getHeaders().entrySet().stream()
            .map(s -> format("%s: %s", s.getKey(), String.join(",", s.getValue())))
            .collect(joining(lineSeparator()));
    String body = Optional.ofNullable(httpRequest.getBody())
            .map(Object::toString)
            .orElse("");
    return format("%s %s HTTP/1.1\n%s\r\n\r\n%s",
            httpRequest.getHttpMethod(),
            httpRequest.getUrl(),
            formattedHeaders,
            body);
  }
}
