package exceptions;

/**
 * Indicates an unrecoverable error inside the service layer of the
 * Stored XSS challenge.
 * <p>
 * Typically wraps SQL exceptions, interrupted database operations,
 * or invalid service state encountered while interacting with the
 * guestbook persistence layer.
 * </p>
 */
public class StoredXssServiceException extends RuntimeException {
    public StoredXssServiceException(String message){
        super(message);
    };

    public StoredXssServiceException(String message, Throwable cause) {
        super(message,cause);
    }
}
