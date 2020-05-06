package endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock;


import endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens.GivenThirdParty;

public class ThirdPartyRequestListener {

    private final GivenThirdParty givenThirdParty;
    final String thirdPartyName;

    public ThirdPartyRequestListener(GivenThirdParty givenThirdParty, String thirdPartyName) {
        this.givenThirdParty = givenThirdParty;
        this.thirdPartyName = thirdPartyName;
    }

    public boolean containsPrimingFor(com.github.tomakehurst.wiremock.http.Request request) {
        return givenThirdParty.remoteMapping().build().getRequest().match(request).isExactMatch();
    }

    public boolean isInScenario() {
        return givenThirdParty.remoteMapping().build().isInScenario();
    }

    @Override
    public String toString() {
        return thirdPartyName + ":" + givenThirdParty.getClass().getSimpleName();
    }
}
