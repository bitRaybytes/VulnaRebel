package database;

import config.Configuration;
import exceptions.DatabaseManagerException;
import logging.Loggers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Factory for PostgreSQL database connections.
 * <p>
 * Reads connection parameters from the provided {@link Configuration}
 * and attempts to establish a connection with configurable retry logic.
 * Each call to {@link #getConnection()} opens and returns a new
 * {@link java.sql.Connection} - the caller is responsible for closing it,
 * preferably via try-with-resources.
 * </p>
 *
 * <pre>{@code
 * try (Connection conn = dbManager.getConnection()) {
 *     // use conn
 * }
 * }</pre>
 */
public class DatabaseManager {
    private static final Logger LOG = Loggers.get(DatabaseManager.class);

    private final Configuration config;

    /**
     * @param config the application configuration containing
     *               {@code database.url}, {@code database.user},
     *               {@code database.pass}, {@code database.retry.max},
     *               and {@code database.retry.delay}
     * @throws DatabaseManagerException if {@code config} is null
     */
    public DatabaseManager(Configuration config){
        if (config == null){
            throw new DatabaseManagerException(DatabaseManager.class.getName() +
                    ": Cannot connect to database without configuration file. ");
        }
        this.config = config;
    }

    /**
     * Opens and returns a live database connection.
     * <p>
     * Retries up to {@code database.retry.max} times with a delay of
     * {@code database.retry.delay} milliseconds between attempts.
     * This guards against the web container starting before PostgreSQL
     * is fully ready to accept connections.
     * </p>
     *
     * @return an open {@link java.sql.Connection} - caller must close it
     * @throws InterruptedException     if the retry sleep is interrupted
     * @throws DatabaseManagerException if all retry attempts are exhausted
     */
    public Connection getConnection() throws InterruptedException {
        int retries = config.getInt("database.retry.max");
        int retryDelay = config.getInt("database.retry.delay");
        Connection connection = null;
        SQLException sqlErrorMsg = null;

        for (int i = 1; i<=retries;i++){
            LOG.fine("Attempting database connection (" + i + "/" + retries + ").");

            try{
                connection = DriverManager.getConnection(
                        config.getString("database.url"), // change here to "database.url.dev" for local database connection
                        config.getString("database.user"),
                        config.getString("database.pass"));

                LOG.fine("Database connection established.");
                return connection;
            }catch (SQLException e) {
                LOG.warning(
                        "Database connection attempt "
                                + i + "/" + retries
                                + " failed: "
                                + e.getMessage());
                Thread.sleep(retryDelay);
                sqlErrorMsg = e;
            }
        }
        LOG.severe(
                "Unable to establish database connection after "
                        + retries + " attempts.");

        throw new DatabaseManagerException(DatabaseManager.class.getName()+":\n"+
                "failed after " + retries +
                " attempts over " + retryDelay*retries +
                " ms ", sqlErrorMsg
        );
    }
}
