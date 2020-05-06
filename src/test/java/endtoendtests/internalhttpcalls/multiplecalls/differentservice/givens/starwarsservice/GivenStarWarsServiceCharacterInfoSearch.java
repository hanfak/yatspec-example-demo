package endtoendtests.internalhttpcalls.multiplecalls.differentservice.givens.starwarsservice;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock.WiremockSetup;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.lang.String.format;

public class GivenStarWarsServiceCharacterInfoSearch extends GivenStarWarsService {

  private static final String FIND_CHARACTER_INFO_BY_ID = "people/";
  // TODO: defaults
  private String personName;
  private String speciesLink;
  private String birthYear;
  private Integer personId;

  public GivenStarWarsServiceCharacterInfoSearch(WiremockSetup wiremockSetup) {
    super(wiremockSetup);
  }

  public GivenStarWarsServiceCharacterInfoSearch willReturn() {
    return this;
  }

  public GivenStarWarsServiceCharacterInfoSearch withPersonName(String personName) {
    this.personName = personName;
    return this;
  }

  public GivenStarWarsServiceCharacterInfoSearch withPersonId(Integer personId) {
    this.personId = personId;
    return this;
  }

  public GivenStarWarsServiceCharacterInfoSearch withBirthYear(String birthYear) {
    this.birthYear = birthYear;
    return this;
  }

  public GivenStarWarsServiceCharacterInfoSearch withSpeciesLink(String speciesLink) {
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
    return STAR_WARS_PATH + FIND_CHARACTER_INFO_BY_ID + personId;
  }

  private String responseBody() {
    return format(PRIMED_RESPONSE_FROM_STARWARS_TEMPLATE, personName, birthYear, speciesLink, personId);
  }

  private static String PRIMED_RESPONSE_FROM_STARWARS_TEMPLATE = "{\n" +
          "  \"name\": \"%s Desilijic Tiure\",\n" +
          "  \"height\": \"175\",\n" +
          "  \"mass\": \"1,358\",\n" +
          "  \"hair_color\": \"n/a\",\n" +
          "  \"skin_color\": \"green-tan, brown\",\n" +
          "  \"eye_color\": \"orange\",\n" +
          "  \"birth_year\": \"%s\",\n" +
          "  \"gender\": \"hermaphrodite\",\n" +
          "  \"homeworld\": \"http://swapi.py4e.com/api/planets/24/\",\n" +
          "  \"films\": [\n" +
          "    \"http://swapi.py4e.com/api/films/1/\",\n" +
          "    \"http://swapi.py4e.com/api/films/3/\",\n" +
          "    \"http://swapi.py4e.com/api/films/4/\"\n" +
          "  ],\n" +
          "  \"species\": [\n" +
          "    \"%s\"\n" +
          "  ],\n" +
          "  \"vehicles\": [ ],\n" +
          "  \"starships\": [ ],\n" +
          "  \"created\": \"2014-12-10T17:11:31.638000Z\",\n" +
          "  \"edited\": \"2014-12-20T21:17:50.338000Z\",\n" +
          "  \"url\": \"http://swapi.py4e.com/api/people/%s/\"\n" +
          "}";
}
