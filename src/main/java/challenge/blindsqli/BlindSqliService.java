package challenge.blindsqli;

import database.DatabaseManager;
import exceptions.BlindSqliServiceException;

import java.sql.SQLException;

/**
 * Service layer for the Blind SQL Injection challenge.
 * <p>
 * Executes deliberately vulnerable SQL queries using string concatenation
 * instead of {@link java.sql.PreparedStatement}.
 * This is intentional - the vulnerability is the learning objective.
 * </p>
 */
public class BlindSqliService {
    private final DatabaseManager manager;

    /**
     * @param manager the database connection factory
     * @throws BlindSqliServiceException if {@code manager} is null
     */
    public BlindSqliService(DatabaseManager manager) {
        if (manager==null){
            throw new BlindSqliServiceException(
                    getClass().getName() +
                            ": DatabaseManager cannot be null."
            );
        }
        this.manager = manager;
    }

    /**
     * Searches for a username using a deliberately vulnerable SQL query.
     * <p>
     * The {@code search} parameter is concatenated directly into the query
     * without sanitization, enabling SQL injection via
     * {@code SUBSTRING}-based boolean payloads.
     * </p>
     *
     * @param search the raw user input - not sanitized
     * @return {@code true} if a matching row was found, {@code false} otherwise
     * @throws BlindSqliServiceException if a SQL or connection error occurs
     */
    public boolean searchAttempt(String search){
        if (search == null || search.isBlank()){
            return false;
        }

        String query =
                "SELECT username FROM blind_users WHERE username = '"
                + search + "';";

        try (var conn = manager.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(query)
        ){
            return rs.next();
        } catch ( SQLException e) {
            throw new BlindSqliServiceException(
                    getClass().getName() +
                            ": A failure in SQL occurred.", e
            );
        } catch (InterruptedException e) {
            throw new BlindSqliServiceException(
                    getClass().getName() +
                            ": A timeout failure connecting to database occurred."
            );
        }

    }
}
