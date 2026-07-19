package exceptions;

/**
 * Thrown when the Blind SQL Injection service
 * cannot execute or process a database operation.
 * <p>
 * Typical causes include SQL execution failures,
 * database communication errors, malformed queries,
 * or unexpected exceptions during challenge processing.
 * </p>
 */
public final class BlindSqliServiceException extends RuntimeException {
    public BlindSqliServiceException(String message) {
        super(message);
    }

    public BlindSqliServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
