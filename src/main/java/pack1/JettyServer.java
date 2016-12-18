package pack1;/*
javac -cp .:javax.servlet-api-3.1.0.jar:jetty-server-9.4.0.v20161208.jar:jetty-util-9.4.0.v20161208.jar:jetty-http-9.4.0.v20161208.jar:jetty-io
-9.4.0.v20161208.jar HelloWorld.java
*/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JettyServer extends AbstractHandler {

    private static final Logger log = LogManager.getLogger();

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException,
            ServletException {
        log.info("info baseRequest:" + baseRequest);
        // Declare response encoding and types
        response.setContentType("text/html; charset=utf-8");

        // Declare response status code
        response.setStatus(HttpServletResponse.SC_OK);

        // Write back response
        response.getWriter().println("<h1>Hello World</h1>");

        // Inform jetty that this request has now been handled
        baseRequest.setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        log.info("log args:" + Arrays.toString(args));
        Server server = new Server(8080);
//        ServletContextHandler context = new ServletContextHandler();
//        new ServletContext("/context",Context.SESSIONS|Context.NO_SECURITY);
//        ServletHolder holder = new ServletHolder();

        server.setHandler(new JettyServer());
//        ServletContextHandler context = new ServletContextHandler(server, "/*", ServletContextHandler.SESSIONS);
//        context.addServlet(new ServletHolder(ServletContainer.class), "/*");
        server.start();
        server.join();
    }
}
