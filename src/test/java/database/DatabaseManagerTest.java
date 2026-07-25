package database;

import config.Configuration;
import config.ConfigurationLoader;
import exceptions.ConfigurationException;
import exceptions.DatabaseManagerException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @Test
    void constructor_shouldThrow_whenFileIsNull() {
        assertThrows(
                DatabaseManagerException.class,
                ()-> new DatabaseManager(null)
        );
    }

    @Test
    void getConnection_shouldReturnValidConnection() throws Exception {
        Configuration config = ConfigurationLoader.load("test-database.properties");
        DatabaseManager manager = new DatabaseManager(config);

        try (Connection connection = manager.getConnection()) {
            assertNotNull(connection);
            assertTrue(connection.isValid(2));
        }
    }

    @Test
    void getConnection_shouldThrow_whenDatabaseUnavailable() {
        Configuration config =
                ConfigurationLoader.load("invalid-database.properties");

        DatabaseManager manager =
                new DatabaseManager(config);

        assertThrows(
                ConfigurationException.class,
                manager::getConnection
        );
    }
}
