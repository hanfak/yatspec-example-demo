package endtoendtests.helper;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class UnirestResponseWrapper {

  private final HttpResponse<String> httpResponse;

  public UnirestResponseWrapper(HttpResponse<String> httpResponse) {
    this.httpResponse = httpResponse;
  }

  public Integer getStatus() {
    return httpResponse.getStatus();
  }

  public String getBody() {
    return httpResponse.getBody();
  }

  public Headers getHeaders() {
    return httpResponse.getHeaders();
  }

  public boolean isSuccessful() {
    return httpResponse.getStatus() >= 200 && httpResponse.getStatus() < 300;
  }

  public String headerValue(String key) {
    return String.join(",", httpResponse.getHeaders().get(key));
  }

  @Override
  public String toString() {
    String formattedHeaders = httpResponse.getHeaders().entrySet().stream()
            .map(s -> format("%s: %s", s.getKey(), String.join(",", s.getValue())))
            .collect(joining(lineSeparator()));
    return format("%s %s %s%n%s%n%n%s", "HTTP/1.1", httpResponse.getStatus(), httpResponse.getStatusText(), formattedHeaders, httpResponse.getBody());

  }
}
