package httpclient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import thirdparty.HttpClient;

public class UnirestHttpClient implements HttpClient {

  public HttpResponse<String> submitGetRequest(String url) throws UnirestException {
    return getHttpRequest(url).asString();
  }

  public HttpRequest getHttpRequest(String url) {
    Unirest.setTimeouts(60000, 30000);
    return Unirest.get(url);
  }
}
