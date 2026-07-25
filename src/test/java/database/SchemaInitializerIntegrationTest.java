package database;

import config.Configuration;
import config.ConfigurationLoader;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SchemaInitializerIntegrationTest {

    @Test
    void initialize_shouldExecuteSqlMultipleTimes_whenCalledMultipleTimes() throws Exception {

        DatabaseManager manager = mock(DatabaseManager.class);
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);

        when(manager.getConnection())
                .thenReturn(conn);

        when(conn.createStatement())
                .thenReturn(stmt);

        SchemaInitializer initializer = new SchemaInitializer(manager);
        initializer.initialize("testchallenge");
        initializer.initialize("testchallenge");

        verify(stmt, atLeast(2))
                .execute(anyString());
    }

    @Test
    void initialize_shouldCreateTableAndSeedData() throws Exception {

        Configuration config =
                ConfigurationLoader.load("test-database.properties");

        DatabaseManager manager = new DatabaseManager(config);

        SchemaInitializer initializer = new SchemaInitializer(manager);
        initializer.initialize("testchallenge");

        try(Connection conn = manager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM test_table"
            )) {

            assertTrue(rs.next());
            assertEquals(1,rs.getInt(1)
            );
        }
    }
}
