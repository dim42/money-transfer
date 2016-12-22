package test.transfer.rest;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static javax.ws.rs.core.Response.Status.OK;

public class JerseyClient {

    public static void main(String[] args) throws UnknownHostException {
        get();
    }

    public static Response get() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        WebTarget target = getWebTarget(String.format("http://%s:%s/", inetAddress.getHostName(), JettyServer.JETTY_PORT));
        Response response = target.request().get();
        if (response.getStatus() != OK.getStatusCode()) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        return response;
    }

    public static WebTarget getWebTarget(String uri) {
        ClientConfig config = new ClientConfig().register(JacksonFeature.class);
        Client client = ClientBuilder.newClient(config);
        return client.target(uri);
    }
}
