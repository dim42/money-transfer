package test.transfer.resources;

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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class JettyServer extends AbstractHandler {

    private static final Logger log = LogManager.getLogger();
    private static final String LOCALHOST = "localhost";
    static final int JETTY_PORT = 8080;

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
        log.info("JettyServer main args:" + Arrays.toString(args));
        Server server = null;
        try {
            InetAddress inetAddress = InetAddress.getByName(LOCALHOST);
            InetSocketAddress address = new InetSocketAddress(inetAddress, JETTY_PORT);
            server = createServer2(address);
            server.start();
            server.join();
        } finally {
            if (server != null) {
                server.destroy();
            }
        }
    }

    public static Server createServer(InetSocketAddress address) {
        Server server = new Server(address);
        server.setHandler(new JettyServer());
        return server;
    }

    public static Server createServer1(InetSocketAddress address) {
        Server server = new Server(address);
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        ServletHolder holder = new ServletHolder(ServletContainer.class);
        holder.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, JettyResource.class.getName());
        context.addServlet(holder, "/*");
        return server;
    }

    public static Server createServer2(InetSocketAddress address) {
        Server server = new Server(address);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        ServletHolder holder = context.addServlet(ServletContainer.class, "/*");
        holder.setInitOrder(0);
        // Tells the Jersey Servlet which REST service/class to load.
        holder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "test.transfer.resources");
        server.setHandler(context);
        return server;
    }
}
