package exceptions;

/**
 * Thrown when the Login SQL Injection service
 * fails while executing challenge-specific logic.
 * <p>
 * Typical causes include database access failures,
 * invalid SQL execution, or unexpected errors during
 * authentication processing.
 * </p>
 */
public final class LoginSqliServiceException extends RuntimeException {
    public LoginSqliServiceException(String message) {
        super(message);
    }
    public LoginSqliServiceException(String message, Throwable cause) {
        super(message,cause);
    }
}
