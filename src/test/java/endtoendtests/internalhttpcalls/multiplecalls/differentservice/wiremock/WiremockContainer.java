package endtoendtests.internalhttpcalls.multiplecalls.differentservice.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.http.Response;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WiremockContainer {

    private static WireMock wireMock;
    private static WireMockServer wireMockServer;
    private static List<RequestListener> requestListeners = new CopyOnWriteArrayList<>();

    static {
        try {
            wireMockServer = wireMockServer();
            wireMockServer.addMockServiceRequestListener(WiremockContainer::handleWireMockRequestResponse);
            wireMockServer.start();
            wireMock = new WireMock(wiremockPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleWireMockRequestResponse(Request request, Response response) {
        for (RequestListener requestListener : requestListeners) {
            requestListener.requestReceived(request, response);
        }
    }

    private static WireMockServer wireMockServer() {
        return new WireMockServer(wireMockConfig()
                .containerThreads(15)
                .port(8888));
    }

    public static int wiremockPort() {
        return wireMockServer.port();
    }

    public static void stopWiremockServer(){
        wireMockServer.stop();
    }

    public static void listen(RequestListener listener) {
            requestListeners.add(listener);
    }

    public static void resetMappings() {
            requestListeners.clear();
            wireMockServer.resetToDefaultMappings();
    }

    public static void register(MappingBuilder MappingBuilder) {
        wireMock.register(MappingBuilder);
    }
}
