package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertEquals;
import static test.transfer.resources.JerseyClient.getWebTarget;
import static test.transfer.resources.JettyServer.JETTY_PORT;
import static test.transfer.resources.ResultCode.OK;

public class JettyTest {

    private static final Logger log = LogManager.getLogger();
    private static final String PROTOCOL = "http";

    private Server server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        InetSocketAddress address = new InetSocketAddress(inetAddress, JETTY_PORT);
        server = JettyServer.createServer(address);
        server.start();

        target = getWebTarget(format("%s://%s:%s", PROTOCOL, inetAddress.getHostName(), JETTY_PORT));
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
    public void testTransfer() {
        log.info("test started");
        String from = "1234";
        String to = "1235";
        String amount = "99.606";
        String cur = "RUR";
        TransferRequest rq = new TransferRequest(from, to, amount, cur);

        TransferResponse response = target.path("transfer/a2a").request(APPLICATION_JSON_TYPE).post(Entity.entity(rq, APPLICATION_JSON_TYPE),
                TransferResponse.class);

        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
    }
}
