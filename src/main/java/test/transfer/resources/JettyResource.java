package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.TransferService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.math.BigDecimal;

import static java.math.RoundingMode.DOWN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static test.transfer.resources.ResultCode.FAIL;
import static test.transfer.resources.ResultCode.OK;

@Path("transfer")
public class JettyResource {
    private static final Logger log = LogManager.getLogger();

    private TransferService transferService = AppContext.getTransferService();

    /**
     * Money transferring from account to account.
     */
    @POST
    @Path("a2a")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse transferA2a(TransferRequest request) {
        log.debug("transfer a2a started");
        try {
            request.validate();
            BigDecimal amount = new BigDecimal(request.getAmount()).setScale(2, DOWN);
            transferService.transfer(request.getFrom(), request.getTo(), amount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(FAIL, e.getMessage());
        }
        return new CommonResponse(OK);
    }

    /**
     * Money transferring from user to account.
     */
    @POST
    @Path("u2a")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse transferU2a(TransferRequest request) {
        try {
            request.validate();
            BigDecimal amount = new BigDecimal(request.getAmount());
            // TODO
            //User from = findUser(request.getFrom());
//            Account fromAcct = null;//fromUser.getAccount();
//            Account toAcct = new Account(request.getTo(), balance, currency, userId, isActive, limit);
//            new Transfer(fromAcct, toAcct).run(amount);
        } catch (Exception e) {
            return new CommonResponse(FAIL, e.getMessage());
        }
        return new CommonResponse(OK);
    }
}
