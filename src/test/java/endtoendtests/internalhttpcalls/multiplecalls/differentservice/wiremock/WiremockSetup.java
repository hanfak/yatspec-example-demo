package endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import endtoendtests.internalhttpcalls.stub.RequestAndResponsesFormatter;
import endtoendtests.internalhttpcalls.stub.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static endtoendtests.internalhttpcalls.stub.YatspecFormatters.toYatspecString;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class WiremockSetup {

  private final RequestAndResponsesFormatter requestAndResponsesFormatter = new RequestAndResponsesFormatter();
  private final Consumer<Request> notifyTestThatActionIsDone = request -> {};
  private final List<ThirdPartyRequestListener> thirdPartyRequestListeners = new ArrayList<>();
  private final TestState testState;

  public WiremockSetup(TestState testState) {
    this.testState = testState;
  }

  public void listenToWireMock(ThirdPartyRequestListener thirdPartyRequestListener) {
    thirdPartyRequestListeners.add(thirdPartyRequestListener);
  }

  public void listenToWiremock() {
    WiremockContainer.listen((request, response) -> {

      notifyTestThatActionIsDone.accept(request);

      List<ThirdPartyRequestListener> listeners = thirdPartyRequestListeners.stream()
              .filter(thirdPartyRequestListener -> thirdPartyRequestListener.containsPrimingFor(request))
              .filter(thirdPartyRequestListener -> !thirdPartyRequestListener.isInScenario())
              .collect(toList());
      if (listeners.size() > 1) {
        throw new IllegalStateException(format("Found more than one listener for request '%s': %s", request.getAbsoluteUrl(), listeners));
      }
      if (listeners.size() == 1) {
        addToCapturedInputsAndOutputs(request, response, listeners.get(0));
      }
    });
  }

  private void addToCapturedInputsAndOutputs(Request request, Response response, ThirdPartyRequestListener thirdPartyRequestListener) {
    RequestResponse requestResponse = requestAndResponsesFormatter.requestResponse("Application", thirdPartyRequestListener.thirdPartyName);
    testState.capturedInputAndOutputs.add(requestResponse.request(), toYatspecString(request));
    testState.capturedInputAndOutputs.add(requestResponse.response(), toYatspecString(response));
  }

  public void stopWiremockServer() {
    WiremockContainer.stopWiremockServer();
  }

  public void resetWireMockMappings() {
    WiremockContainer.resetMappings();
    configureDefaultMappings();
  }

  public void configureDefaultMappings() {
    WiremockContainer.register(any(urlMatching("/.*")).willReturn(aResponse().withBody("No responses matched for the given request").withStatus(999)));
  }

  public String wiremockPrimings() {
    // TODO use property
    String url = "http://localhost:8888" + "/__admin/";
    try {
      return Unirest.get(url).asString().getBody();
    } catch (UnirestException e) {
      throw new RuntimeException(format("Could not retrieve the wiremock primings from '%s'", url));
    }
  }
}
