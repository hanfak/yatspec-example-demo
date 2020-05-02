package endtoendtests.fluentthen.thens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import endtoendtests.helper.UnirestResponseWrapper;

import static endtoendtests.fluentthen.thens.CapturedInputAndOutputKeys.RESPONSE_FROM_APPLICATION;

public class Then {
  private final TestState interactions;

  public Then(TestState interactions) {
    this.interactions = interactions;
  }

  public ResponseMatcher theResponse() {
    UnirestResponseWrapper response = interactions.capturedInputAndOutputs.getType(RESPONSE_FROM_APPLICATION, UnirestResponseWrapper.class);
    return new ResponseMatcher(response);
  }
}
