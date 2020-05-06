package endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens.starwarsservice;

import endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens.GivenThirdParty;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock.WiremockSetup;

public abstract class GivenStarWarsService extends GivenThirdParty {
    public static final String STAR_WARS_PATH = "/starwars/";

    public GivenStarWarsService(WiremockSetup wiremockSetup) {
        super(wiremockSetup);
    }

    @Override
    protected String thirdPartyName() {
        return "Star Wars Service";
    }
}
