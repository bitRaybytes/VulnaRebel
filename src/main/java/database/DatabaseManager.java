package database;

import config.Configuration;
import exceptions.DatabaseManagerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/// This is class is for returning a database connection.</br>
/// It is recommended to call a connection in a try-with-resources block.
/// </br></br>
/// f.e:</br>
/// `try (Connection conn = databaseManager.getConnection()){
///     // use conn
/// }`
public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    private final Configuration config;

    public DatabaseManager(Configuration config){
        if (config == null){
            LOGGER.warning("No configuration file detected. ");
            throw new DatabaseManagerException(DatabaseManager.class.getName() +
                    ": Cannot connect to database without configuration file. ");
        }
        this.config = config;
    }

    public Connection getConnection() throws InterruptedException {
        int retries = config.getInt("database.retry.max");
        int retryDelay = config.getInt("database.retry.delay");
        Connection connection = null;
        SQLException sqlErrorMsg = null;

        for (int i = 1; i<=retries;i++){
            try{
                connection = DriverManager.getConnection(
                        config.getString("database.url"),
                        config.getString("database.user"),
                        config.getString("database.pass"));

                return connection;
            }catch (SQLException e) {
                Thread.sleep(retryDelay);
                sqlErrorMsg = e;
            }
        }

        throw new DatabaseManagerException(DatabaseManager.class.getName()+":\n"+
                "failed after " + retries +
                " attempts over " + retryDelay*retries +
                " ms ", sqlErrorMsg
        );
    }
}
