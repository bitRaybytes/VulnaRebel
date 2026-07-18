package exceptions;

/**
 * Thrown when the Reflected XSS handler
 * cannot process or render the challenge page.
 * <p>
 * Typical causes include invalid request parameters,
 * template rendering failures, or malformed input
 * during request processing.
 * </p>
 */
public final class ReflectedXssHandlerException extends RuntimeException {
    public ReflectedXssHandlerException(String message) {
        super(message);
    }

    public ReflectedXssHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
