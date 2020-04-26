package endtoendtests.reqandresponly.capturedinputsandoutputs;

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

  @Override
  public String toString() {
    String formattedHeaders = httpResponse.getHeaders().entrySet().stream()
            .map(s -> format("%s: %s", s.getKey(), String.join(",", s.getValue())))
            .collect(joining(lineSeparator()));
    return format("%s %s%n%s%n%n%s", "HTTP", httpResponse.getStatus(), formattedHeaders, httpResponse.getBody());

  }
}
