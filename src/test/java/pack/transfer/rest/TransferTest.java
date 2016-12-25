package pack.transfer.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pack.transfer.dao.DbTest;
import pack.transfer.rest.dto.AccountRequest;
import pack.transfer.rest.dto.CommonResponse;
import pack.transfer.rest.dto.TransferRequest;
import pack.transfer.rest.dto.UserRequest;

import javax.ws.rs.client.WebTarget;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static java.lang.String.format;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertEquals;
import static pack.transfer.rest.JerseyClient.getWebTarget;
import static pack.transfer.rest.JettyServer.JETTY_PORT;
import static pack.transfer.rest.dto.ResultCode.FAIL;
import static pack.transfer.rest.dto.ResultCode.OK;
import static pack.transfer.rest.resources.AccountResource.*;
import static pack.transfer.rest.resources.TransferResource.TRANSFER;
import static pack.transfer.rest.resources.UserResource.USER;

public class TransferTest {

    private static final Logger log = LogManager.getLogger();
    private static final String PROTOCOL = "http";
    private static final String FROM_ACC = "1234";
    private static final String TO_ACC = "2222";
    private static final String NOT_ACTIVE_ACC = "3333";
    private static final String CUR = "RUB";
    private static final String EUR_ACC = "4444";

    private Server server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        DbTest.cleanTestDB();
        DbTest.createTestDB();
        DbTest.insertCurRates();

        InetAddress inetAddress = InetAddress.getLocalHost();
        InetSocketAddress address = new InetSocketAddress(inetAddress, JETTY_PORT);
        server = JettyServer.createServer(address);
        server.start();

        target = getWebTarget(format("%s://%s:%s", PROTOCOL, inetAddress.getHostName(), JETTY_PORT));

        insertUsersAndAccounts();
    }

    private void insertUsersAndAccounts() {
        long user1Id = 1L;
        long user2Id = 2L;
        UserRequest uRq = new UserRequest(user1Id, "user1");
        target.path(USER + "/" + CREATE).request(APPLICATION_JSON_TYPE).post(entity(uRq, APPLICATION_JSON_TYPE), CommonResponse.class);
        uRq = new UserRequest(user2Id, "user2");
        target.path(USER + "/" + CREATE).request(APPLICATION_JSON_TYPE).post(entity(uRq, APPLICATION_JSON_TYPE), CommonResponse.class);

        AccountRequest aRq = new AccountRequest(FROM_ACC, "120.24", CUR, user1Id, true, "100");
        target.path(ACCOUNT + "/" + CREATE).request(APPLICATION_JSON_TYPE).post(entity(aRq, APPLICATION_JSON_TYPE), CommonResponse.class);
        aRq = new AccountRequest(TO_ACC, "250.67", CUR, user2Id, true, "260");
        target.path(ACCOUNT + "/" + CREATE).request(APPLICATION_JSON_TYPE).post(entity(aRq, APPLICATION_JSON_TYPE), CommonResponse.class);
        aRq = new AccountRequest(NOT_ACTIVE_ACC, "300", CUR, user2Id, false, "150");
        target.path(ACCOUNT + "/" + CREATE).request(APPLICATION_JSON_TYPE).post(entity(aRq, APPLICATION_JSON_TYPE), CommonResponse.class);
        aRq = new AccountRequest(EUR_ACC, "400", "EUR", user2Id, true, "130");
        target.path(ACCOUNT + "/" + CREATE).request(APPLICATION_JSON_TYPE).post(entity(aRq, APPLICATION_JSON_TYPE), CommonResponse.class);
    }

    @After
    public void tearDown() throws Exception {
        try {
            server.stop();
        } finally {
            server.destroy();
        }
//        DbTest.cleanTestDB();
    }

    @Test
    public void testTransfer() {
        log.debug("test started");
        TransferRequest rq = new TransferRequest(FROM_ACC, TO_ACC, "99.606", CUR);

        CommonResponse response = target.path(TRANSFER + "/a2a").request(APPLICATION_JSON_TYPE).post(entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
        response = target.path(ACCOUNT + "/" + FIND).queryParam("number", FROM_ACC).request(APPLICATION_JSON_TYPE).get(CommonResponse.class);
        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
        assertEquals("20.64", response.getMessage());
        response = target.path(ACCOUNT + "/" + FIND).queryParam("number", TO_ACC).request(APPLICATION_JSON_TYPE).get(CommonResponse.class);
        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
        assertEquals("350.27", response.getMessage());
    }

    @Test
    public void testTransfer_cur() {
        TransferRequest rq = new TransferRequest(FROM_ACC, EUR_ACC, "1.2", "USD");

        CommonResponse response = target.path(TRANSFER + "/a2a").request(APPLICATION_JSON_TYPE).post(entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
        response = target.path(ACCOUNT + "/" + FIND).queryParam("number", FROM_ACC).request(APPLICATION_JSON_TYPE).get(CommonResponse.class);
        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
        assertEquals("46.84", response.getMessage());
        response = target.path(ACCOUNT + "/" + FIND).queryParam("number", EUR_ACC).request(APPLICATION_JSON_TYPE).get(CommonResponse.class);
        assertEquals(response.getMessage(), OK.toString(), response.getResultCode());
        assertEquals("401.25", response.getMessage());
    }

    @Test
    public void testTransfer_theSameAccount() {
        TransferRequest rq = new TransferRequest(FROM_ACC, FROM_ACC, "99.606", CUR);

        CommonResponse response = target.path(TRANSFER + "/a2a").request(APPLICATION_JSON_TYPE).post(entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), FAIL.toString(), response.getResultCode());
        assertEquals("Accounts are equal", response.getMessage());
    }

    @Test
    public void testTransfer_negativeAmount() {
        TransferRequest rq = new TransferRequest(FROM_ACC, TO_ACC, "-99.606", CUR);

        CommonResponse response = target.path(TRANSFER + "/a2a").request(APPLICATION_JSON_TYPE).post(entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), FAIL.toString(), response.getResultCode());
        assertEquals("Amount is negative", response.getMessage());
    }

    @Test
    public void testTransfer_exceedsLimit() {
        TransferRequest rq = new TransferRequest(FROM_ACC, TO_ACC, "101", CUR);

        CommonResponse response = target.path(TRANSFER + "/a2a").request(APPLICATION_JSON_TYPE).post(entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), FAIL.toString(), response.getResultCode());
        assertEquals("Transfer amount (101.00) exceeds account's (1234) limit (100.00)", response.getMessage());
    }

    @Test
    public void testTransfer_notActiveAccount() {
        TransferRequest rq = new TransferRequest(FROM_ACC, NOT_ACTIVE_ACC, "100", CUR);

        CommonResponse response = target.path(TRANSFER + "/a2a").request(APPLICATION_JSON_TYPE).post(entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), FAIL.toString(), response.getResultCode());
        assertEquals("Account (3333) is not active", response.getMessage());
    }


    @Test
    public void testTransfer_insufficientBalance() {
        TransferRequest rq = new TransferRequest(TO_ACC, FROM_ACC, "251", CUR);

        CommonResponse response = target.path(TRANSFER + "/a2a").request(APPLICATION_JSON_TYPE).post(entity(rq, APPLICATION_JSON_TYPE),
                CommonResponse.class);

        assertEquals(response.getMessage(), FAIL.toString(), response.getResultCode());
        assertEquals("Insufficient balance (250.67) for account:2222, required 251.00", response.getMessage());
    }
}
