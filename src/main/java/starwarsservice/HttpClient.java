package starwarsservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.io.IOException;

public interface HttpClient {
  HttpResponse<String> submitGetRequest(String url) throws UnirestException, IOException;
  HttpRequest getHttpRequest(String url);
}
