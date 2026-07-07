package http;

import com.sun.net.httpserver.HttpHandler;
import config.Configuration;
import exceptions.VulnaHttpServerException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/// This class owns an underlying `HttpServer` lifecycle (create, start, stopp, add contexts).</br>
/// If calling a `VulnaHttpServer` object the server will get implicitly instantiated.</br>

public class VulnaHttpServer {
    /// @param delay the delay it takes to stop the server.
    /// @param server the `HttpServer` to act as the instance.
    private static final Logger LOGGER = Logger.getLogger(VulnaHttpServer.class.getName());
    private final int delay;
    private final com.sun.net.httpserver.HttpServer server;


    public VulnaHttpServer(Configuration config) throws IOException {
        if (config == null){
            throw new VulnaHttpServerException(
                    VulnaHttpServer.class.getName() +
                            ": Config file cannot be null."
            );
        }

        int port    = config.getInt("server.port");
        int threads = config.getInt("server.execute.threads");
        delay = config.getInt("server.stop.delay");
        server = com.sun.net.httpserver.HttpServer.create(
                new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(threads)); // concurrent request handling
    }

    public void start() {
        server.start();
        LOGGER.info("Server started on port " + server.getAddress().getPort());
    }

    public void stop() {
        if (server != null) {
            server.stop(delay);
        }
    }

    public void applyRoutes(Router router) {
        for (Route route : router.getRoutes()) {
            addContext(route.getPath(), route.getHandler());
        }
    }

    private void addContext(String path, HttpHandler handler) {
        server.createContext(path, handler);
    }
}
