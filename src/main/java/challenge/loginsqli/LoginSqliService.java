package challenge.loginsqli;

import database.DatabaseManager;
import exceptions.LoginSqliServiceException;

import java.sql.SQLException;

/**
 * Service layer for the SQL Injection challenge.
 * <p>
 * Executes deliberately vulnerable SQL queries using string concatenation
 * instead of {@link java.sql.PreparedStatement}.
 * This is intentional - the vulnerability is the learning objective.
 * </p>
 */
public class LoginSqliService {
    private final DatabaseManager manager;

    public LoginSqliService(DatabaseManager manager) throws LoginSqliServiceException {
        if (manager== null){
            throw new LoginSqliServiceException(
                    getClass().getName() +
                            ": DatabaseManager cannot be null."
            );
        }
        this.manager = manager;
    }

    /**
     * The method runs a deliberately vulnerable SQL query.<br>
     * <p>
     *     Attempts to log in with {@code username} and {@code password}.
     *     <br>
     *     Parameters are directly concatenated into the query without
     *     sanitization, enabling SQL injection f.e. via {@code 'OR '1'='1}-based payloads.
     * </p>
     * String concatenation isn't sanitized, no `PreparedStatement` is used intentionally.<br>
     * @param username the raw user input - not sanitized
     * @param password the raw user input - not sanitized
     * @return {@code true} if {@code username} and {@code password} found
     * or {@code false} if both wrong.
     * @throws InterruptedException if database connection attempts are exhausted
     * @throws LoginSqliServiceException when challenge related errors occur
     */
    public boolean attemptLogin(String username, String password)
            throws InterruptedException, LoginSqliServiceException {

        if (username == null || username.isBlank() ||
            password == null || password.isBlank())
        {
            return false;
        }

        String query =
                "SELECT * FROM login_users WHERE username = '" + username +
                        "' AND password = '" + password + "'";

        try (var conn = manager.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(query)) {
            return rs.next();
        } catch (SQLException e) {
            throw new LoginSqliServiceException(
                    LoginSqliService.class.getName() +
                            ": SQL query failure.",e);
        }
    }
}
