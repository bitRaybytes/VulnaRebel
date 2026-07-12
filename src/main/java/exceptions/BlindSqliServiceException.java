package exceptions;


import java.io.IOException;
import java.sql.SQLException;

public class BlindSqliServiceException extends RuntimeException {
    public BlindSqliServiceException(String message) {
        super(message);
    }

    public BlindSqliServiceException(String message, SQLException e) {
        super(message, e);
    }

    public BlindSqliServiceException(String message, IOException e) {
        super(message,e);
    }

    public BlindSqliServiceException(String message, Exception e) {
        super(message,e);
    }
}
