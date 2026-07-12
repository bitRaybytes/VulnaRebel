package core;

import exceptions.ApplicationException;
import http.Router;
import http.VulnaHttpServer;

import java.util.logging.Logger;

/**
 * Orchestrates the VulnaRebel application lifecycle.
 * <p>
 * Receives a fully configured {@link VulnaHttpServer} and {@link Router},
 * applies all registered routes, and manages server startup and shutdown.
 * All challenge wiring is performed in {@link core.Main} before this
 * class is constructed - {@code Application} has no knowledge of
 * individual challenges.
 * </p>
 */
public class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    private final VulnaHttpServer server;
    private final Router router;

    /**
     * @param server the configured HTTP server
     * @param router the router holding all registered challenge routes
     * @throws IllegalArgumentException if either parameter is null
     */
    public Application(VulnaHttpServer server, Router router) {
        validate(server,router);
        this.server = server;
        this.router = router;
    }

    /**
     * Applies all registered routes to the server and starts listening
     * for incoming HTTP connections.
     */
    public void start() {
        server.applyRoutes(router);
        server.start();
        LOG.info(getClass().getName() + " – VulnaRebel started.");
    }

    /**
     * Stops the HTTP server and releases its socket binding.
     */
    public void stop() {
        server.stop();
        LOG.info(getClass().getName() + " – VulnaRebel stopped.");
    }

    private void validate(VulnaHttpServer server, Router router) {
        if (server == null){
            throw new ApplicationException(
                    getClass().getName() +
                            ": Server cannot be null."
            );
        }
        if (router == null){
            throw new ApplicationException(
                    getClass().getName() +
                            ": Router cannot be null."
            );
        }
    }
}
