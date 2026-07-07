package exceptions;

import java.io.IOException;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, IOException e) {
        super(message, e);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
