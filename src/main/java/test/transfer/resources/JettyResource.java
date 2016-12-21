package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.model.Account;
import test.transfer.model.Transfer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.math.BigDecimal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static test.transfer.resources.ResultCode.FAIL;
import static test.transfer.resources.ResultCode.OK;

@Path("transfer")
public class JettyResource {

    private static final Logger log = LogManager.getLogger();

    @POST
    @Path("a2a")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public TransferResponse transferA2a(TransferRequest request) {
        log.info("request:" + request);
        try {
            request.validate();
            BigDecimal amount = new BigDecimal(request.getAmount());
            Account fromAcct = new Account(request.getFrom());
            Account toAcct = new Account(request.getTo());
            new Transfer(fromAcct, toAcct).transferMoney(amount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new TransferResponse(FAIL, e.getMessage());
        }
        return new TransferResponse(OK);
    }

    @POST
    @Path("u2a")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public TransferResponse transferU2a(TransferRequest request) {
        try {
            request.validate();
            BigDecimal amount = new BigDecimal(request.getAmount());
            // TODO
            //User from = findUser(request.getFrom());
            Account fromAcct = null;//fromUser.getAccount();
            Account toAcct = new Account(request.getTo());
            new Transfer(fromAcct, toAcct).transferMoney(amount);
        } catch (Exception e) {
            return new TransferResponse(FAIL, e.getMessage());
        }
        return new TransferResponse(OK);
    }
}
