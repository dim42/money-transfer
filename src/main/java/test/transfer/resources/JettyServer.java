package test.transfer.resources;/*
javac -cp .:javax.servlet-api-3.1.0.jar:jetty-server-9.4.0.v20161208.jar:jetty-util-9.4.0.v20161208.jar:jetty-http-9.4.0.v20161208.jar:jetty-io
-9.4.0.v20161208.jar HelloWorld.java
*/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

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
        Server server = null;
        try {
            server = startServer();
            server.join();
        } finally {
            if (server != null) {
                server.destroy();
            }
        }
    }

    public static Server startServer() throws Exception {
        Server server = new Server(8080);
        server.setHandler(new JettyServer());
        server.start();
        return server;
    }

    public static Server startServer1() throws Exception {
        Server server = new Server(8080);
//        ServletContextHandler context = new ServletContextHandler();
//        new ServletContext("/context",Context.SESSIONS|Context.NO_SECURITY);
//        ServletHolder holder = new ServletHolder();

//        server.setHandler(new JettyServer());
        ServletHolder holder = new ServletHolder(ServletContainer.class);
        holder.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, JettyResource.class.getCanonicalName());
        ServletContextHandler context = new ServletContextHandler(server, "/*", ServletContextHandler.SESSIONS);
        context.addServlet(holder, "/*");
        server.start();
        return server;
    }

    public static Server startServer2() throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

//        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/webapi/*");
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "resources");

//        ServletHolder staticServlet = context.addServlet(DefaultServlet.class, "/*");
//        staticServlet.setInitParameter("resourceBase", "src/main/webapp");
//        staticServlet.setInitParameter("pathInfoOnly", "true");

        try {
            server.start();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        return server;
    }

    public static Server startServer3() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server server = new Server(8080);
        server.setHandler(context);

        ServletHolder holder = context.addServlet(ServletContainer.class, "/*");
        holder.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        holder.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, JettyResource.class.getCanonicalName());
//        holder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, JettyResource.class.getCanonicalName());

        server.start();
        return server;
    }

    public static Server startServer4() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server server = new Server(8080);
        server.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", JettyResource.class.getCanonicalName());

        server.start();
        return server;
    }
}
