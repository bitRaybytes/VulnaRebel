package challenge.login;

import database.DatabaseManager;
import exceptions.LoginServiceException;

import java.sql.SQLException;

public class LoginService {
    private final DatabaseManager manager;

    public LoginService(DatabaseManager manager) {
        this.manager = manager;
    }

    /// The `attemptLogin` method runs a deliberately vulnerable SQL query.<br>
    /// String concatenation isn't sanitized, no `PreparedStatement` is used intentionally.<br>
    /// Returns true or false and is used in `LoginHandler`
    public boolean attemptLogin(String username, String password)
            throws InterruptedException, LoginServiceException {

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
            throw new LoginServiceException(
                    LoginService.class.getName() +
                            ": SQL query failure.",e);
        }
    }
}
