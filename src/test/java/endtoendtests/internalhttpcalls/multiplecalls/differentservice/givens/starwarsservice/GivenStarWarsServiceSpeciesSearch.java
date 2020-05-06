package endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens.starwarsservice;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock.WiremockSetup;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.lang.String.format;

public class GivenStarWarsServiceSpeciesSearch extends GivenStarWarsService {

    private static final String FIND_SPECIES_BY_ID = "species/5"; // TODO species id as builder
    // TODO: defaults
    private String speciesName;
    private String speciesLink;

    public GivenStarWarsServiceSpeciesSearch(WiremockSetup wiremockSetup) {
        super(wiremockSetup);
    }

    public GivenStarWarsServiceSpeciesSearch willReturn() {
        return this;
    }

    public GivenStarWarsServiceSpeciesSearch withSpeciesName(String speciesName) {
        this.speciesName = speciesName;
        return this;
    }

    public GivenStarWarsServiceSpeciesSearch withSpeciesLink(String speciesLink) {
        this.speciesLink = speciesLink;
        return this;
    }

    @Override
    public MappingBuilder remoteMapping() {
        return get(urlPathEqualTo(urlPath()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(responseBody()));
    }

    @Override
    protected String urlPath() {
        return STAR_WARS_PATH + FIND_SPECIES_BY_ID;
    }

    private String responseBody() {
        return format(STAR_WARS_SPECIES_INFO_RESPONSE, speciesName, speciesLink);

    }

    private static String STAR_WARS_SPECIES_INFO_RESPONSE = "{\n" +
            "    \"average_height\": \"2.1\",\n" +
            "    \"average_lifespan\": \"400\",\n" +
            "    \"classification\": \"Mammal\",\n" +
            "    \"created\": \"2014-12-10T16:44:31.486000Z\",\n" +
            "    \"designation\": \"Sentient\",\n" +
            "    \"edited\": \"2014-12-10T16:44:31.486000Z\",\n" +
            "    \"eye_colors\": \"blue, green, yellow, brown, golden, red\",\n" +
            "    \"hair_colors\": \"black, brown\",\n" +
            "    \"homeworld\": \"https://swapi.py4e.com/api/planets/14/\",\n" +
            "    \"language\": \"Shyriiwook\",\n" +
            "    \"name\": \"%s\",\n" +
            "    \"people\": [\n" +
            "        \"https://swapi.py4e.com/api/people/13/\"\n" +
            "    ],\n" +
            "    \"films\": [\n" +
            "        \"https://swapi.py4e.com/api/films/1/\",\n" +
            "        \"https://swapi.py4e.com/api/films/2/\"\n" +
            "    ],\n" +
            "    \"skin_colors\": \"gray\",\n" +
            "    \"url\": \"%s\"\n" +
            "}";
}
