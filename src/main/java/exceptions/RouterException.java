package exceptions;

/**
 * Thrown when the {@link http.Router} fails to register
 * or manage application routes.
 * <p>
 * Typical causes include duplicate route registrations,
 * null routes, or invalid router state.
 * </p>
 */
public final class RouterException extends RuntimeException {
    public RouterException(String message) {
        super(message);
    }

    public RouterException(String message, Throwable cause) {
        super(message, cause);
    }
}
