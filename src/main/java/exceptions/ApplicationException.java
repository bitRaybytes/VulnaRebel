package exceptions;

import java.io.IOException;

public class ApplicationException extends Throwable {
    public ApplicationException(String message, IOException e) {
        super(message, e);
    }

}
