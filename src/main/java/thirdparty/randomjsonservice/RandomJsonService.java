package thirdparty.randomjsonservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import domain.Person;
import logging.LoggingCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Settings;
import thirdparty.HttpClient;

import java.io.IOException;

public class RandomJsonService {

  private final static Logger APPLICATION_LOGGER = LoggerFactory.getLogger(LoggingCategory.APPLICATION.name());

  private final HttpClient httpClient;
  private final Settings settings;

  public RandomJsonService(HttpClient httpClient, Settings settings) {
    this.httpClient = httpClient;
    this.settings = settings;
  }

  public Person getCharacterInfo(String id) throws IOException {
    try {
      String apiAddress = settings.randomJsonApiAddress();
      HttpResponse<String> response = httpClient.submitGetRequest(apiAddress + "posts/" + id);
      // tODO new return type, for get
      // Go to xml api https://fakerestapi.azurewebsites.net/swagger/ui/index#!/Activities/Activities_Get
      return null;
    } catch (UnirestException e) {
      String message = "Unexpected exception when getting data from api";
      APPLICATION_LOGGER.error(message, e);
      throw new IllegalStateException(message, e);
    }
  }

  // TODO do a post
}
