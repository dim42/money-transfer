package pack1;/*
javac -cp .:javax.servlet-api-3.1.0.jar:jetty-server-9.4.0.v20161208.jar:jetty-util-9.4.0.v20161208.jar:jetty-http-9.4.0.v20161208.jar:jetty-io
-9.4.0.v20161208.jar HelloWorld.java
*/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
//import javax.ws.rs.Pu

public class Contr {

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
