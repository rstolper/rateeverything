package com.romanstolper.rateeverything.startup;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    //public static final String BASE_URI = "http://localhost:8080/app/";
    public static final String HOST = (System.getenv("HOST")!=null?System.getenv("HOST"):"localhost");
    public static final String PORT = (System.getenv("PORT")!=null?System.getenv("PORT"):"8080");
    public static final String BASE_URI = "http://"+HOST+":"+PORT+"/app/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.romanstolper.rateeverything package
        final ResourceConfig rc = new ResourceConfig().packages("com/romanstolper/rateeverything");
        rc.register(JacksonFeature.class);

        //Injector inject = Guice.createInjector(new ItemModule());

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        StaticHttpHandler websiteHandler = new StaticHttpHandler("ui");
        websiteHandler.setFileCacheEnabled(false);
        server.getServerConfiguration().addHttpHandler(websiteHandler);
        return server;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("$PORT: " + System.getenv("PORT"));
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\n"
                //+ "Hit enter to stop it..."
                , BASE_URI));
        //server.wait();
        System.in.read(); // just run it forever till the process dies
        System.in.read(); // just run it forever till the process dies
        System.out.println("read two lines of input");
        while(true) {
            Thread.sleep(1000000);
        }
        //System.out.println("finished waiting, shutting down server");
        //server.shutdown();
//        while (true) {
//            Thread.sleep(100000);
//        }
    }
}

