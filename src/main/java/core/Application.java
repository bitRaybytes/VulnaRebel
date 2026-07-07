package core;

import exceptions.ApplicationException;
import http.Router;
import http.VulnaHttpServer;

import java.util.logging.Logger;

public class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    private final VulnaHttpServer server;
    private final Router router;

    public Application(VulnaHttpServer server, Router router) {
        validate(server,router);
        this.server = server;
        this.router = router;
    }

    public void start() {
        server.applyRoutes(router);
        server.start();
        LOG.info(getClass().getName() + " – VulnaRebel started.");
    }

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
