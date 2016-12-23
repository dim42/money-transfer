package pack.transfer.rest.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.CurRateService;
import pack.transfer.rest.AppContext;
import pack.transfer.rest.dto.CommonResponse;
import pack.transfer.rest.dto.CurRateRequest;
import pack.transfer.rest.dto.ResultCode;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static pack.transfer.rest.resources.CurRateResource.CUR_RATE;

@Path(CUR_RATE)
public class CurRateResource {
    public static final String CUR_RATE = "cur_rate";
    public static final String CREATE = "create";
    private static final Logger log = LogManager.getLogger();

    private final CurRateService curRateService = AppContext.getCurRateService();

    @POST
    @Path(CREATE)
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public CommonResponse create(CurRateRequest request) {
        log.debug("create cur_rate started");
        try {
            curRateService.create(request.getId(), request.getC2c(), request.getRate());
            return new CommonResponse(ResultCode.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(ResultCode.FAIL, e.getMessage());
        }
    }
}
