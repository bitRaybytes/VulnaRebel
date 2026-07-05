package exceptions;

import java.sql.SQLException;

public class LoginServiceException extends Throwable {
    public LoginServiceException(String message) {
        super(message);
    }
    public LoginServiceException(String message, SQLException e) {
        super(message,e);
    }
}
