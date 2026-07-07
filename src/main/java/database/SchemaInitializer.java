package database;

import config.Configuration;
import exceptions.SchemaInitializerException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

/// This class' purpose is to initialize a challenge's table in the database. </br>
/// It will also read the modules database dummy data in seeds.sql and  </br>
/// The connection will be established by DatabaseManagers `getConnection()` Method.  </br> </br>
/// Executes SQL scripts
public class SchemaInitializer {

    private final DatabaseManager manager;
    private final Configuration challengeConfig;

    // TODO: read challenge-specific db settings related to name, multiple queries...

    public SchemaInitializer(
            DatabaseManager manager,
            Configuration challengeConfig){
        this.manager = manager;
        this.challengeConfig = challengeConfig;
    }

    public void initialize(String challengeName) throws SchemaInitializerException {
        if (challengeName == null || challengeName.isBlank()){
            throw new SchemaInitializerException(
                    SchemaInitializer.class.getName() +
                            ": Given parameter cannot be null."
            );
        }

        try (Connection conn = manager.getConnection()) {
            executeSqlFile(conn, "challenges/" + challengeName + "/schema.sql");
            executeSqlFile(conn, "challenges/" + challengeName + "/seed.sql");
        } catch (SQLException | InterruptedException e) {
            throw new SchemaInitializerException(
                    SchemaInitializer.class.getName()+
                            ": Failed to initialize database table.", e);
        }
    }

    private void executeSqlFile(Connection conn, String path) throws SchemaInitializerException {
        try (InputStream is = SchemaInitializer.class.getResourceAsStream("/" + path)) {

            if (is == null) throw new SchemaInitializerException(
                    SchemaInitializer.class.getName() + ": Resource not found: " + path);

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            for (String statement : content.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    try (var stmt = conn.createStatement()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new SchemaInitializerException(
                    SchemaInitializer.class.getName()+
                            ": Failed to read sql file.", e);
        }
    }

}
