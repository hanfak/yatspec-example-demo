package starwarsservice;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import domain.Person;
import domain.Species;
import logging.LoggingCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StarWarsService {
  private final static Logger APPLICATION_LOGGER = LoggerFactory.getLogger(LoggingCategory.APPLICATION.name());

  private final HttpClient httpClient;
  // Settings for url

  public StarWarsService(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public Person getCharacterInfo(String id) throws IOException {
    HttpResponse<String> response = null;// TODO Can be param
    try {
      response = httpClient.submitGetRequest("http://swapi.py4e.com/api/people/" + id);
    } catch (UnirestException e) {
      APPLICATION_LOGGER.error("error getting info", e);
    }
    String body = response.getBody();
    DocumentContext personJson = JsonPath.parse(body);
    String species = personJson.read("$.species[0]");
    String name = personJson.read("$.name");
    ;
    String birthYear = personJson.read("$.birth_year");
    return new Person(species, name, birthYear); // change
  }


  public Species getSpeciesInfo(String url) throws IOException {
    HttpResponse<String> response = null;// TODO Can be param
    try {
      response = httpClient.submitGetRequest(url);
    } catch (UnirestException e) {
      APPLICATION_LOGGER.error("error getting info", e);
    }
    String body = response.getBody();
    DocumentContext speciesJson = JsonPath.parse(body);

    String name = speciesJson.read("$.name");
    Integer lifeSpan = Integer.parseInt(speciesJson.read("$.average_lifespan"));
    Double averageHeight = Double.parseDouble(speciesJson.read("$.average_height"));
    return new Species(name, lifeSpan, averageHeight);
  }
}
