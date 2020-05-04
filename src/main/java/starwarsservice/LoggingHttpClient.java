package starwarsservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import logging.LoggingCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.lang.String.format;

public class LoggingHttpClient implements HttpClient {

  private final static Logger APPLICATION_LOGGER = LoggerFactory.getLogger(LoggingCategory.APPLICATION.name());
  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final HttpClient delegate;
  private final HttpLoggingFormatter httpLoggingFormatter;

  public LoggingHttpClient(HttpClient delegate, HttpLoggingFormatter httpLoggingFormatter) {
    this.delegate = delegate;
    this.httpLoggingFormatter = httpLoggingFormatter;
  }

  @Override
  public HttpResponse<String> submitGetRequest(String url) throws UnirestException, IOException {
    return tryToExecuteRequest(url);
  }

  @Override
  public HttpRequest getHttpRequest(String url) {
    return delegate.getHttpRequest(url);
  }

  private HttpResponse<String> tryToExecuteRequest(String url) throws UnirestException, IOException {
    HttpRequest httpRequest = getHttpRequest(url);
    String requestUrl = httpRequest.getUrl();
    logRequest(httpRequest, requestUrl);
    try {
      HttpResponse<String> response = delegate.submitGetRequest(url);
      logResponse(requestUrl, response);
      return response;
    } catch (RuntimeException exception) {
      logError(httpRequest, requestUrl, exception);
      throw exception;
    }
  }

  private void logRequest(HttpRequest httpRequest, String requestUrl) throws IOException {
    String formattedRequest = httpLoggingFormatter.requestOutput(httpRequest);
    AUDIT_LOGGER.info(format("Request from Application to %s\n%s", requestUrl, formattedRequest));
  }

  private void logResponse(String requestUrl, HttpResponse<String> response) {
    String formattedResponse = httpLoggingFormatter.responseOutput(response);
    AUDIT_LOGGER.info(format("Response from %s to Application received \n%s", requestUrl, formattedResponse));
  }

  private void logError(HttpRequest httpRequest, String requestUrl, RuntimeException exception) {
    APPLICATION_LOGGER.error(format("Failed to execute request from Application to %s \n%s", requestUrl, httpRequest), exception);
  }
}
