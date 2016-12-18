package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static test.transfer.resources.JerseyClient.getWebTarget;
import static test.transfer.resources.JettyServer.JETTY_PORT;

public class JettyTest {

    private static final Logger log = LogManager.getLogger();

    private Server server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        InetSocketAddress address = new InetSocketAddress(inetAddress, JETTY_PORT);
        server = JettyServer.createServer(address);
        server.start();

        target = getWebTarget(String.format("http://%s:%s/", inetAddress.getHostName(), JETTY_PORT));
    }

    @After
    public void tearDown() throws Exception {
        try {
            server.stop();
        } finally {
            server.destroy();
        }
    }

    @Test
    public void testGet() {
        Response response = target.request().get();

        assertEquals(OK.getStatusCode(), response.getStatus());
        log.error("readEntity response:" + response.readEntity(String.class));
    }
}
