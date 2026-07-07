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
    private static final Logger LOGGER = Logger.getLogger(VulnaHttpServer.class.getName());
    private final int delay;
    private final com.sun.net.httpserver.HttpServer server;


    /// @param config provides the configuration properties.
    public VulnaHttpServer(Configuration config) throws IOException {
        if (config == null){
            throw new VulnaHttpServerException(
                    getClass().getName() +
                            ": Configuration file cannot be null."
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
        if (server == null) {
            throw new VulnaHttpServerException(
                    getClass().getName() +
                            ": Cannot start, because server is null."
            );
        }
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
        if (path == null || path.isBlank()){
            throw new VulnaHttpServerException(
                    getClass().getName() + ": Context path cannot be null."
            );
        }
        if (handler == null){
            throw new VulnaHttpServerException(
                    getClass().getName() +
                            ": HttpHandler cannot be null."
            );
        }
        server.createContext(path, handler);
    }
}
