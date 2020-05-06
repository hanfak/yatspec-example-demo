package endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock.ThirdPartyRequestListener;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock.WiremockContainer;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock.WiremockSetup;


public abstract class GivenThirdParty implements GivensBuilder {
  private final WiremockSetup wiremockSetup;

  protected GivenThirdParty(WiremockSetup wiremockSetup) {
    this.wiremockSetup = wiremockSetup;
  }

  @Override
  public InterestingGivens build(InterestingGivens givens) {
    WiremockContainer.register(remoteMapping());
    listenToWireMock();
    return givens;
  }

  private void listenToWireMock() {
    wiremockSetup.listenToWireMock(new ThirdPartyRequestListener(this, thirdPartyName()));
  }

  public abstract MappingBuilder remoteMapping();

  protected abstract String thirdPartyName();

  protected abstract String urlPath();
}
