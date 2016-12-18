package test.transfer.resources;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertNotNull;

public class JettyTest {

    private Server server;

    @Before
    public void setUp() throws Exception {
        server = JettyServer.startServer();
    }

    @After
    public void tearDown() throws Exception {
        server.destroy();
    }

    @Test
    public void testGet() {
        Response response = JerseyClient.get();
        System.out.println("response:" + response.readEntity(String.class));
        assertNotNull(response);
    }
}
