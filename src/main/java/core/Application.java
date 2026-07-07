package core;

import http.Router;
import http.VulnaHttpServer;

import java.util.logging.Logger;

public class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    private final VulnaHttpServer server;
    private final Router router;

    public Application(VulnaHttpServer server, Router router) {
        this.server = server;
        this.router = router;
    }

    public void start() {
        server.applyRoutes(router);
        server.start();
        LOG.info("VulnaRebel started.");
    }

    public void stop() {
        server.stop();
        LOG.info("VulnaRebel stopped.");
    }

}
