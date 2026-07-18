package exceptions;

import java.io.IOException;

/**
 * Thrown when the application fails during startup
 * or while initializing core components.
 * <p>
 * Typical causes include failures while configuring the HTTP server,
 * registering routes, or reading required application resources.
 * </p>
 */
public final class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
