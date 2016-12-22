package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static test.transfer.resources.ResultCode.FAIL;
import static test.transfer.resources.ResultCode.OK;

@Path("account")
public class AccountResource {
    private static final Logger log = LogManager.getLogger();

    private AccountService accountService = AppContext.getAccountService();

    @POST
    @Path("create")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse create(AccountRequest request) {
        log.debug("create account started");
        try {
            request.validate();
            accountService.create(request.getNumber(), request.getBalance(), request.getCurrency(), request.getUserId(), request.isActive(), request.getLimit
                    ());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(FAIL, e.getMessage());
        }
        return new CommonResponse(OK);
    }
}
