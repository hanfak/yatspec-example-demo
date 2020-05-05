package endtoendtests.internalhttpcalls.stub;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.mashape.unirest.http.Headers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
// Nicer output
public class YatspecFormatters {

    public static String toYatspecString(Request wireMockRequest) {
        return String.format("%s %s HTTP/1.1%s", wireMockRequest.getMethod().getName(), wireMockRequest.getUrl(), "\n")
                + formatHeader(adaptHeaders(wireMockRequest.getHeaders()))
                + "\r\n\r\n"
                + adaptBody(wireMockRequest.getBodyAsString());
    }

    public static String toYatspecString(Response wiremockResponse) {
        return String.format("%s %s%n%s%n%n%s", "HTTP/1.1", wiremockResponse.getStatus(),
                fixWiremockListenerProblem(adaptHeaders(wiremockResponse.getHeaders())),
                adaptBody(wiremockResponse.getBodyAsString()));
    }

    // Can use these instead of UnirestRequestWrapper
    public static String requestOutput(String uri, Map<String, String> headers, String body) {
        return format("%s %s HTTP/1.1%s", "POST", uri, "\n") + headersFormatter(headers) + "\r\n\r\n" + body;
    }

    // Can use these instead of UnirestResponseWrapper
    public static String responseOutput(int responseStatus, Headers responseHeaders, String responseBody) {
        return format("%s %s%n%s%n%n%s", "HTTP", responseStatus, headersFormatter(responseHeaders), responseBody);
    }
    private static String headersFormatter(Map<String, String> headers) {
        return headers.entrySet().stream()
                .map(s -> format("%s: %s", s.getKey(), s.getValue()))
                .collect(joining(lineSeparator()));
    }

    private static String headersFormatter(Headers headers) {
        return headers.entrySet().stream()
                .map(s -> format("%s: %s", s.getKey(), s.getValue()))
                .collect(joining(lineSeparator()));
    }

    private static String adaptBody(String wiremockBody) {
        if (wiremockBody == null) {
            return "";
        } else {
            return wiremockBody;
        }
    }

    private static String fixWiremockListenerProblem(List<Header> headers) {
        return headers.stream().map(YatspecFormatters::appendGzipToEtag).collect(Collectors.toList()).stream()
                .map(header -> format("%s: %s", header.key, String.join(",", header.value)))
                .collect(joining(lineSeparator()));
    }

    private static String formatHeader(List<Header> headers) {
        return headers.stream().map(header -> format("%s: %s", header.key, String.join(",", header.value)))
                .collect(joining(lineSeparator()));
    }

    private static Header appendGzipToEtag(Header header) {
        return header.key.toLowerCase().equals("etag") ? new Header(header.key, header.value + "--gzip")  : header;
    }

    private static List<Header> adaptHeaders(HttpHeaders headers) {
        return headers.all().stream()
                .map(httpHeader -> new Header(httpHeader.key(), httpHeader.values()))
                .collect(Collectors.toList());
    }

    private static class Header {

        final String key;
        public final String value;

        Header(String key, String value) {
            this.key = key;
            this.value = value;
        }

        Header(String key, List<String> values) {
            this.key = key;
            this.value = values.stream().collect(joining(","));
        }

        @Override
        public String toString() {
            return key + ": " + value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Header header = (Header) o;

            if (!key.equals(header.key)) return false;
            return value.equals(header.value);
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }
    }
}
