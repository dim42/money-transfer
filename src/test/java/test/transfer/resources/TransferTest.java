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

public class TransferTest {

    private static final Logger log = LogManager.getLogger();
    private static final String PROTOCOL = "http";

    private Server server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        DbTest.cleanTestDB();
        DbTest.createTestDB();

        InetAddress inetAddress = InetAddress.getLocalHost();
        InetSocketAddress address = new InetSocketAddress(inetAddress, JETTY_PORT);
        server = JettyServer.createServer(address);
        server.start();

        target = getWebTarget(format("%s://%s:%s", PROTOCOL, inetAddress.getHostName(), JETTY_PORT));

        insertTestData();
    }

    private void insertTestData() {
        long user1Id = 1L;
        long user2Id = 2L;
        UserRequest uRq = new UserRequest(user1Id, "user1");
        CommonResponse response = target.path("user/create").request(APPLICATION_JSON_TYPE).post(Entity.entity(uRq, APPLICATION_JSON_TYPE),
                CommonResponse.class);
        uRq = new UserRequest(user2Id, "user2");
        response = target.path("user/create").request(APPLICATION_JSON_TYPE).post(Entity.entity(uRq, APPLICATION_JSON_TYPE),
                CommonResponse.class);
        AccountRequest aRq = new AccountRequest("1234", "120.24", "RUR", user1Id, true, "100");
        response = target.path("account/create").request(APPLICATION_JSON_TYPE).post(Entity.entity(aRq, APPLICATION_JSON_TYPE),
                CommonResponse.class);
        aRq = new AccountRequest("2222", "250.67", "RUR", user2Id, true, "150");
        response = target.path("account/create").request(APPLICATION_JSON_TYPE).post(Entity.entity(aRq, APPLICATION_JSON_TYPE),
                CommonResponse.class);
    }

    @After
    public void tearDown() throws Exception {
        try {
            server.stop();
        } finally {
            server.destroy();
        }
        DbTest.cleanTestDB();
    }

    @Test
    public void testTransfer() {
        log.info("test started");
        String from = "1234";
        String to = "1235";
        String amount = "99.606";
        String cur = "RUR";
        TransferRequest rq = new TransferRequest(from, to, amount, cur);

        CommonResponse response = target.path("transfer/a2a").request(APPLICATION_JSON_TYPE).post(Entity.entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
    }
}
