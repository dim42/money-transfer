package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static test.transfer.resources.ResultCode.FAIL;
import static test.transfer.resources.ResultCode.OK;
import static test.transfer.resources.UserResource.USER;

@Path(USER)
public class UserResource {
    public static final String USER = "user";
    private static final Logger log = LogManager.getLogger();

    private UserService userService = AppContext.getUserService();

    @POST
    @Path("create")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse create(UserRequest request) {
        log.debug("create user started");
        try {
            request.validate();
            userService.create(request.getId(), request.getName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(FAIL, e.getMessage());
        }
        return new CommonResponse(OK);
    }
}
