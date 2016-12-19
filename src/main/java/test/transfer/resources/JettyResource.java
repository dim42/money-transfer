package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.model.Account;
import test.transfer.model.Transfer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static test.transfer.resources.ResultCode.FAIL;
import static test.transfer.resources.ResultCode.OK;

@Path("transfer")
public class JettyResource {

    private static final Logger log = LogManager.getLogger();

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String root() {
        log.info("root:");
        return "jetty root";
    }

    @POST
    @Path("a2a")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public TransferResponse transfer(TransferRequest request) {
        log.info("request:" + request);
        try {
            request.validate();
            BigDecimal amount = new BigDecimal(request.getAmount());
            Account fromAcct = new Account(request.getFrom());
            Account toAcct = new Account(request.getTo());
            new Transfer(fromAcct, toAcct).transferMoney(amount);
        } catch (Exception e) {
            return new TransferResponse(FAIL, e.getMessage());
        }
        return new TransferResponse(OK, null);
    }

    @GET
    @Path("/get2")
    public String get2() {
        log.info("get2:");
        return "str";
    }

    @PUT
    @Path("/put")
    public void put() {
        log.info("put:");
    }
}
