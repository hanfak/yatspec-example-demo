package thirdparty.starwarsservice;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import domain.Person;
import domain.Species;
import logging.LoggingCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Settings;
import thirdparty.HttpClient;
import webserver.StarWarsInterfaceService;

import java.io.IOException;

public class StarWarsService implements StarWarsInterfaceService {

  private final static Logger APPLICATION_LOGGER = LoggerFactory.getLogger(LoggingCategory.APPLICATION.name());

  private final HttpClient httpClient;
  private final Settings settings;

  public StarWarsService(HttpClient httpClient, Settings settings) {
    this.httpClient = httpClient;
    this.settings = settings;
  }

  @Override
  public Person getCharacterInfo(String id) throws IOException {
    try {
      String apiAddress = settings.starWarsApiAddress();
      HttpResponse<String> response = httpClient.submitGetRequest(apiAddress + "people/" + id);
      String body = response.getBody();
      DocumentContext personJson = JsonPath.parse(body);
      String species = personJson.read("$.species[0]");
      String name = personJson.read("$.name");
      String birthYear = personJson.read("$.birth_year");
      return new Person(species, name, birthYear); // change
    } catch (UnirestException e) {
      String message = "Unexpected exception when getting data from api";
      APPLICATION_LOGGER.error(message, e);
      throw new IllegalStateException(message, e);
    }
  }

  @Override
  public Species getSpeciesInfo(String url) throws IOException {
    try {
      HttpResponse<String> response = httpClient.submitGetRequest(url);
      String body = response.getBody();
      DocumentContext speciesJson = JsonPath.parse(body);

      String name = speciesJson.read("$.name");
      Integer lifeSpan = Integer.parseInt(speciesJson.read("$.average_lifespan"));
      Double averageHeight = Double.parseDouble(speciesJson.read("$.average_height"));
      return new Species(name, lifeSpan, averageHeight);
    } catch (UnirestException e) {
      String message = "Unexpected exception when getting data from api";
      APPLICATION_LOGGER.error(message, e);
      throw new IllegalStateException(message, e);
    }
  }
}
