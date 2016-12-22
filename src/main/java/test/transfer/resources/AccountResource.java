package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.AccountService;
import test.transfer.model.Account;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static test.transfer.resources.AccountResource.ACCOUNT;
import static test.transfer.resources.ResultCode.FAIL;
import static test.transfer.resources.ResultCode.OK;

@Path(ACCOUNT)
public class AccountResource {
    public static final String ACCOUNT = "account";
    public static final String CREATE = "create";
    public static final String FIND = "find";
    private static final Logger log = LogManager.getLogger();

    private AccountService accountService = AppContext.getAccountService();

    @POST
    @Path(CREATE)
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse create(AccountRequest request) {
        log.debug("create account started");
        try {
            request.validate();
            accountService.create(request.getNumber(), request.getBalance(), request.getCurrency(), request.getUserId(), request.isActive(), request.getLimit
                    ());
            return new CommonResponse(OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(FAIL, e.getMessage());
        }
    }

    @GET
    @Path(FIND)
//    @Consumes({APPLICATION_JSON, APPLICATION_XML})
//    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse find(@QueryParam("number") String number) {
        log.debug("find account started");
        try {
            Account account = accountService.find(number);
            return new CommonResponse(OK, account.getBalance().toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(FAIL, e.getMessage());
        }
    }
}
