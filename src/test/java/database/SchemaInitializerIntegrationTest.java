package database;

import config.Configuration;
import config.ConfigurationLoader;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SchemaInitializerIntegrationTest {
    @Test
    void initialize_shouldThrow_whenIdempotent(){
        DatabaseManager manager = mock(DatabaseManager.class);
        Connection conn = mock(Connection.class);
        Statement stmt  = mock(Statement.class);

        try {
            when(manager.getConnection()).thenReturn(conn);
            when(conn.createStatement()).thenReturn(stmt);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        SchemaInitializer initializer =
                new SchemaInitializer(manager);

        initializer.initialize("testchallenge");
        initializer.initialize("testchallenge");
        initializer.initialize("testchallenge");

    }

    @Test
    void initialize_shouldCreateTableAndSeedData() throws Exception {

        Configuration config =
                ConfigurationLoader.load("test-database.properties");

        DatabaseManager manager =
                new DatabaseManager(config);


        SchemaInitializer initializer =
                new SchemaInitializer(manager);


        initializer.initialize("testchallenge");


        try(Connection conn = manager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM test_table"
            )) {


            assertTrue(rs.next());

            assertEquals(
                    1,
                    rs.getInt(1)
            );
        }
    }
}
