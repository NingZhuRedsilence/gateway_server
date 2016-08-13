/**
 * Created by user on 7/1/16.
 */


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


public class SimpleServer {
    public static void main(String[] args) throws Exception {
        //create server
        Server server = new Server(8080);

        // The ServletHandler is a dead simple way to create a context handler
        // that is backed by an instance of a Servlet.
        // This handler then needs to be registered with the Server object.
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        //one handler for root
        handler.addServletWithMapping(HelloServlet.class, "/");

        //one handler for hello
        handler.addServletWithMapping(HelloServlet.class, "/hello");

        //one handler for '/world'
        handler.addServletWithMapping(WorldServlet.class, "/world");

        //one handler for '/note'
        handler.addServletWithMapping(NoteServlet.class, "/note");

        //one handler for '/dart'
        handler.addServletWithMapping(NoteServlet.class, "/dart");

// Add the filter, and then use the provided FilterHolder to configure it
        // Setup the context for servlets
        FilterHolder cors = handler.addFilterWithMapping(CrossOriginFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        server.start();
//join() is blocking until server is ready. It behaves like Thread.join()
        server.join();

    }

}
