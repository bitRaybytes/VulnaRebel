package exceptions;

/**
 * Thrown when the embedded HTTP server cannot be
 * created, configured, or started successfully.
 * <p>
 * Typical causes include invalid server configuration,
 * port binding failures, or HTTP context registration
 * errors.
 * </p>
 */
public final class VulnaHttpServerException extends RuntimeException {
    public VulnaHttpServerException(String message) {
        super(message);
    }

    public VulnaHttpServerException(String message, Throwable cause) {
        super(message,cause);
    }
}
