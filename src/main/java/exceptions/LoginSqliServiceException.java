package exceptions;

import java.sql.SQLException;

public class LoginSqliServiceException extends RuntimeException {
    public LoginSqliServiceException(String message) {
        super(message);
    }
    public LoginSqliServiceException(String message, SQLException e) {
        super(message,e);
    }
}
