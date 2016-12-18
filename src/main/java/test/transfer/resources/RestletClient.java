package test.transfer.resources;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

public class RestletClient {

    private static final String serverHost = "127.0.0.1";
    private static final int serverPort = 8080;
    private final String serverUrl = String.format("http://%s:%s/", serverHost, serverPort);
    //    private final String rootPath = "central.web/";
    private final String rootPath = "";

    public static void main(String[] args) throws Exception {
        new RestletClient().get("get");
    }

    public void get(String operation) throws Exception {
        Client client = new Client(new Context(), Protocol.HTTP);
        String uri = serverUrl + rootPath + operation;
        ClientResource resource = new ClientResource(uri);
        resource.setNext(client);
        String string = "{\"ipv4Addr\":\"106.109.9.68\"}";
        String text = string;
        Representation output = new StringRepresentation(text, MediaType.APPLICATION_JSON);
        //output.setMediaType(MediaType.APPLICATION_JSON);
        Representation representation = resource.get();
        System.out.println(representation.getText());
    }

    public void post(String operation) throws Exception {
        // Component component = new Component();
        // component.getServers().add(Protocol.HTTP, 8081);
        //
        // // Then attach it to the local host
        // component.getDefaultHost().attach("/", RestProcessor.class);
        //
        // // Now, let's start the component!
        // // Note that the HTTP server connector is also automatically started.
        // component.start();
        Client client = new Client(new Context(), Protocol.HTTP);
        String uri = serverUrl + rootPath + operation;
        ClientResource resource = new ClientResource(uri);
        resource.setNext(client);
        String string = "{\"ipv4Addr\":\"106.109.9.68\"}";
        String text = string;
        Representation output = new StringRepresentation(text, MediaType.APPLICATION_JSON);
        //output.setMediaType(MediaType.APPLICATION_JSON);
        Representation representation = resource.post(output);
        System.out.println(representation.getText());
    }
}
