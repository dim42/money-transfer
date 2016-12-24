package pack.transfer.rest.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.UserService;
import pack.transfer.rest.AppContext;
import pack.transfer.rest.dto.CommonResponse;
import pack.transfer.rest.dto.ResultCode;
import pack.transfer.rest.dto.UserRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static pack.transfer.rest.resources.UserResource.USER;

@Path(USER)
public class UserResource {
    public static final String USER = "user";
    private static final Logger log = LogManager.getLogger();

    private final UserService userService = AppContext.getUserService();

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
            return new CommonResponse(ResultCode.FAIL, e.getMessage());
        }
        return new CommonResponse(ResultCode.OK);
    }
}
