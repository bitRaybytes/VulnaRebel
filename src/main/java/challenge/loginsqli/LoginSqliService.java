package challenge.loginsqli;

import database.DatabaseManager;
import exceptions.LoginSqliServiceException;

import java.sql.SQLException;

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

    /** The {@code attemptLogin()} method runs a deliberately vulnerable SQL query.<br>
     * String concatenation isn't sanitized, no `PreparedStatement` is used intentionally.<br>
     * @return true or false and is used in `LoginSqliHandler`
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
