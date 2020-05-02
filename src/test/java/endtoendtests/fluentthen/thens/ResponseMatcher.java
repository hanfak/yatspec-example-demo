package endtoendtests.fluentthen.thens;

import endtoendtests.helper.UnirestResponseWrapper;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class ResponseMatcher {
  private final UnirestResponseWrapper response;

  public ResponseMatcher(UnirestResponseWrapper response) {
    this.response = response;
  }

  public ResponseMatcher hasStatusCodeOf(int expectedStatusCode) {
    assertThat(response.getStatus()).isEqualTo(expectedStatusCode);
    return this;
  }

  public ResponseMatcher isSuccessful() {
    // This logic can be passed here or in the response
    assertThat(response.isSuccessful()).describedAs("is successful").isTrue();
    return this;
  }

  public ResponseMatcher isNotSuccessful() {
    assertThat(response.isSuccessful()).describedAs("is not successful").isFalse();
    return this;
  }


  public ResponseMatcher hasBodyOf(String expectedBody) {
    assertThat(response.getBody()).isEqualTo(expectedBody);
    return this;
  }

  public ResponseMatcher bodyContains(String string) {
    if (!response.getBody().contains(string)) {
      Assertions.fail(format("%nCould not find '%s' in body:%n%s%n", string, response.getBody()));
    }
    return this;
  }
  public ResponseMatcher hasBodyWhichContains(String expectedBody) {
    assertThat(response.getBody()).contains(expectedBody);
    return this;
  }

  public ResponseMatcher hasContentTypeContaining(String expectedContentType) {
    Optional<String> actualContentType = response.getHeaders().entrySet().stream()
            .filter(header -> header.getKey().contains("Content-Type"))
            .map(Map.Entry::getValue)
            .flatMap(List::stream)
            .filter(headerValue -> headerValue.startsWith(expectedContentType))
            .findFirst();
    assertThat(actualContentType).isPresent();
    return this;
  }

  public ResponseHeaderMatcher hasHeader(String headerKey) {
    return new ResponseHeaderMatcher(this, response, headerKey);
  }

  public ResponseMatcher isPlainText() {
    return hasContentTypeContaining("text/html");
  }

  // a helper function
  public ResponseMatcher and() {
    return this;
  }

  // Can be extracted a separate class
  public static class ResponseHeaderMatcher {

    private final ResponseMatcher parentMatcher;
    private final UnirestResponseWrapper response;
    private final String headerKey;

    public ResponseHeaderMatcher(ResponseMatcher parentMatcher, UnirestResponseWrapper response, String headerKey) {
      this.parentMatcher = parentMatcher;
      this.response = response;
      this.headerKey = headerKey;
    }

    public ResponseMatcher withHeaderValue(String headerValue) {
      assertThat(response.headerValue(headerKey)).isEqualTo(headerValue);
      return parentMatcher; // So can chain assertions in calling class
    }

    public ResponseMatcher hasHeaderValueStartingWith(String headerValue) {
      assertThat(response.headerValue(headerKey)).startsWith(headerValue);
      return parentMatcher;
    }

    public ResponseMatcher hasHeaderValueContaining(String headerValue) {
      assertThat(response.headerValue(headerKey)).contains(headerValue);
      return parentMatcher;
    }

    public ResponseMatcher isTexthtml() {
      assertThat(response.headerValue(headerKey)).isEqualTo("text/html");
      return parentMatcher;
    }

    // In case it is a random value which cannot be mocked or dont care about actual value
    public ResponseHeaderMatcher hasHeaderThatMatches(String regex) {
      assertThat(response.headerValue(headerKey)).matches(regex);
      return this; // So can chain assertions in the class
    }

    public ResponseMatcher containsOnlyAlphaNumericAndDashCharacters() {
      assertThat(response.headerValue(headerKey)).matches("^[a-zA-Z0-9\\-]+$");
      return parentMatcher;
    }

    // a helper function
    public ResponseHeaderMatcher and() {
      return this;
    }

    public ResponseMatcher andAllHeadersHaveBeenAssertedOn() {
      return parentMatcher;
    }
  }
}
