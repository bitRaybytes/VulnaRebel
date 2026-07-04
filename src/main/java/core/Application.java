package core;

import com.sun.net.httpserver.HttpServer;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import http.VulnaHttpServer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Application {
    private static final Logger LOG = Logger.getLogger(Application.class.getName());
    // this class is used to orchestrate following
    // - Configuration
    // - Database
    // - HTTP
    // - Challenges


    // all method as voids:

    public void start() {

//        loadConfiguration();

        connectDatabase();
    /*
        initializeDatabase();

        createRouter();

        registerRoutes();
*/
        startHttpServer();

    }

    private void startHttpServer() {
//        VulnaHttpServer server = new VulnaHttpServer()
    }

    private void connectDatabase() {
        Configuration config = ConfigurationLoader.load("application.properties");
        try {
            Connection connection = new DatabaseManager(config).getConnection();

            LOG.info(Application.class.getName() + "\n" +
                    "Database Version: " + connection.getMetaData().getDatabaseProductVersion() + "\n"
                            + "Database Url: " + connection.getMetaData().getURL());

        } catch (InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadConfiguration() {
    }

}
