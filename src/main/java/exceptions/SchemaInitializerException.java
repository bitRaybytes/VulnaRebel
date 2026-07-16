package exceptions;

import java.sql.SQLException;

public class SchemaInitializerException extends RuntimeException {
    public SchemaInitializerException(String message) {
        super(message);
    }

    public SchemaInitializerException(String message, Exception e) {
        super(message,e);
    }

}
