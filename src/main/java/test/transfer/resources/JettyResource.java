package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

public class JettyResource {

    private static final Logger log = LogManager.getLogger();

    @GET
    @Path("/")
    public String root() {
        log.info("root:");
        return "str";
    }

    @GET
    @Path("get")
    public String get() {
        log.info("get:");
        return "str";
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
