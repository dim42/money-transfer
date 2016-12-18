package test.transfer.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class JettyServer {

    private static final Logger log = LogManager.getLogger();
    private static final String LOCALHOST = "localhost";
    static final int JETTY_PORT = 8080;

    public static void main(String[] args) throws Exception {
        log.info("JettyServer main args:" + Arrays.toString(args));
        Server server = null;
        try {
            InetAddress inetAddress = InetAddress.getByName(LOCALHOST);
            InetSocketAddress address = new InetSocketAddress(inetAddress, JETTY_PORT);
            server = createServer(address);
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
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        ServletHolder holder = new ServletHolder(ServletContainer.class);
        // Tells the Jersey Servlet which REST service/class to load.
        holder.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, JettyResource.class.getCanonicalName());
        context.addServlet(holder, "/*");
        return server;
    }
}
