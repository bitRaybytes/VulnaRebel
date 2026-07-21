package exceptions;

/**
 * Indicates an unrecoverable error while processing HTTP requests
 * for the Stored XSS challenge.
 * <p>
 * This exception wraps failures related to request handling,
 * response generation, or invalid handler state.
 * </p>
 */
public class StoredXssHandlerException extends RuntimeException {
    public StoredXssHandlerException(String message) {
        super(message);
    }

    public StoredXssHandlerException(String message, Throwable cause){
        super(message,cause);
    }
}
