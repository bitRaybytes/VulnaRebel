package database;

import config.Configuration;
import exceptions.SchemaInitializerException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Initializes the database schema and seed data for a challenge module.
 * <p>
 * On startup, each SQL-backed challenge calls {@link #initialize(String)}
 * with its challenge name. This class resolves the corresponding
 * {@code schema.sql} and {@code seed.sql} files from the classpath,
 * executes them sequentially on a single {@link java.sql.Connection},
 * and closes the connection when done.
 * </p>
 * <p>
 * SQL files are split on {@code ;} and executed statement by statement.
 * {@code CREATE TABLE IF NOT EXISTS} and
 * {@code INSERT ... ON CONFLICT DO NOTHING} ensure idempotent execution -
 * safe to run on every application startup without duplicating data.
 * </p>
 * <p>
 * Expected classpath layout for a challenge named {@code login}:
 * </p>
 * <pre>
 * src/main/resources/challenges/login/schema.sql
 * src/main/resources/challenges/login/seed.sql
 * </pre>
 */
public class SchemaInitializer {

    private final DatabaseManager manager;
    private final Configuration challengeConfig;

    /**
     * @param manager         the database connection factory -
     *                        used to open a connection for SQL execution
     * @param challengeConfig the challenge-specific configuration -
     *                        reserved for future challenge-specific
     *                        database settings such as
     *                        {@code challenge.db.allow_multi_queries}
     * @throws SchemaInitializerException if either parameter is null
     */
    public SchemaInitializer(
            DatabaseManager manager,
            Configuration challengeConfig) throws SchemaInitializerException {
        if (manager == null){
            throw new SchemaInitializerException(
                    getClass().getName() + ": DatabaseManager cannot be null."
            );
        }
        if (challengeConfig == null){
            throw new SchemaInitializerException(
                    getClass().getName() + ": Challenge's config cannot be null."
            );
        }
        this.manager = manager;
        this.challengeConfig = challengeConfig;
    }

    /**
     * Executes the {@code schema.sql} and {@code seed.sql} files
     * for the given challenge name.
     * <p>
     * Both files share a single {@link java.sql.Connection} - if either
     * fails, the connection is closed and a
     * {@link SchemaInitializerException} is thrown before the application
     * starts serving traffic.
     * </p>
     *
     * @param challengeName the challenge folder name used to resolve
     *                      SQL file paths, e.g. {@code "login"} resolves
     *                      to {@code challenges/login/schema.sql}
     *                      and {@code challenges/login/seed.sql}
     * @throws SchemaInitializerException if {@code challengeName} is null
     *                                    or blank, if either SQL file is
     *                                    not found on the classpath,
     *                                    or if SQL execution fails
     */
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

    // Reads a single SQL file from the classpath, splits it on semicolons,
    // and executes each non-empty statement on the provided connection.
    // Shares the caller's connection - no new connection is opened here.
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
