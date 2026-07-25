package database;

import exceptions.SchemaInitializerException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SchemaInitializerTest {

    @Test
    void constructor_shouldThrow_whenDatabaseManagerIsNull() {
        assertThrows(
                SchemaInitializerException.class,
                () -> new SchemaInitializer(null)
        );
    }

    @Test
    void initialize_shouldThrow_whenChallengeNameIsNull() {
        DatabaseManager manager = mock(DatabaseManager.class);

        SchemaInitializer initializer =
                new SchemaInitializer(manager);

        assertThrows(
                SchemaInitializerException.class,
                () -> initializer.initialize(null)
        );
        verifyNoInteractions(manager);
    }

    @Test
    void initialize_shouldThrow_whenChallengeNameIsBlank() {

        DatabaseManager manager = mock(DatabaseManager.class);

        SchemaInitializer initializer =
                new SchemaInitializer(manager);

        assertThrows(
                SchemaInitializerException.class,
                () -> initializer.initialize("   ")
        );

        verifyNoInteractions(manager);
    }

    @Test
    void initialize_shouldThrow_whenSqlFilesDoNotExist(){

        DatabaseManager manager = mock(DatabaseManager.class);

        Connection connection = mock(Connection.class);

        try {
            when(manager.getConnection())
                    .thenReturn(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SchemaInitializer initializer =
                new SchemaInitializer(manager);

        assertThrows(
                SchemaInitializerException.class,
                () -> initializer.initialize("does-not-exist")
        );

        try {
            verify(manager).getConnection();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void initialize_shouldExecuteSqlStatements() throws Exception {

        DatabaseManager manager = mock(DatabaseManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);

        when(manager.getConnection())
                .thenReturn(connection);

        when(connection.createStatement())
                .thenReturn(statement);

        SchemaInitializer initializer =
                new SchemaInitializer(manager);

        initializer.initialize("testchallenge");

        verify(statement, atLeastOnce())
                .execute(anyString());
    }

    @Test
    void initialize_shouldCloseDatabaseConnection() throws Exception {

        DatabaseManager manager = mock(DatabaseManager.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);

        when(manager.getConnection())
                .thenReturn(connection);

        when(connection.createStatement())
                .thenReturn(statement);

        SchemaInitializer initializer =
                new SchemaInitializer(manager);

        initializer.initialize("testchallenge");

        verify(connection).close();
    }




}
