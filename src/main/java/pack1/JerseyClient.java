package pack1;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class JerseyClient {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
//        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        Client client = ClientBuilder.newClient(config);
        // Client client = Client.create();
//        WebResource webResource = client.resource("http://localhost:8080/central.web/scanToBox");
//        WebResource webResource = client.resource("http://localhost:8080/get");
        WebTarget webResource = client.target("http://localhost:8080/get");
        String input = "{\"ipv4Addr\":\"106.109.9.68\"}";
//        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
//                .post(ClientResponse.class, input);
//        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
//        ClientResponse response = webResource.get(ClientResponse.class);
        Response response = webResource.request().get();

//        GenericResponseDto entity = response.getEntity(GenericResponseDto.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        // 2.0 Jersey version
        // Client client2 = ClientBuilder.newClient();
        // WebTarget target = client2.target("http://localhost:9998").path("resource");
        // Response postResponse = target.request(MediaType.TEXT_PLAIN_TYPE).post(
        // Entity.entity("A string entity to be POSTed", MediaType.TEXT_PLAIN));
    }
}

//class GenericResponseDto {
//
//    @JsonProperty("returnCode")
//    private String returnCode;
//}
