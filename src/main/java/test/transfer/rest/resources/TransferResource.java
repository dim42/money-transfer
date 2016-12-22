package test.transfer.rest.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.TransferService;
import test.transfer.rest.AppContext;
import test.transfer.rest.dto.CommonResponse;
import test.transfer.rest.dto.TransferRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static test.transfer.rest.dto.ResultCode.FAIL;
import static test.transfer.rest.dto.ResultCode.OK;
import static test.transfer.rest.resources.TransferResource.TRANSFER;

@Path(TRANSFER)
public class TransferResource {
    private static final Logger log = LogManager.getLogger();
    public static final String TRANSFER = "transfer";

    private final TransferService transferService = AppContext.getTransferService();

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
            transferService.transfer(request.getFrom(), request.getTo(), request.getAmountNum());
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
            //TODO
//            transferService.transferU2a(request.getFrom(), request.getTo(), request.getAmountNum());
        } catch (Exception e) {
            return new CommonResponse(FAIL, e.getMessage());
        }
        return new CommonResponse(OK);
    }

    /**
     * Money transferring from account to user .
     */
    @POST
    @Path("a2u")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse transferA2u(TransferRequest request) {
        try {
            request.validate();
            //TODO
//            transferService.transferA2u(request.getFrom(), request.getTo(), request.getAmountNum());
        } catch (Exception e) {
            return new CommonResponse(FAIL, e.getMessage());
        }
        return new CommonResponse(OK);
    }

    /**
     * Money transferring from user to user.
     */
    @POST
    @Path("u2u")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse transferU2u(TransferRequest request) {
        try {
            request.validate();
            //TODO
//            transferService.transferU2u(request.getFrom(), request.getTo(), request.getAmountNum());
        } catch (Exception e) {
            return new CommonResponse(FAIL, e.getMessage());
        }
        return new CommonResponse(OK);
    }

}
