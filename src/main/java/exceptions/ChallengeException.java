package exceptions;

/**
 * Thrown when a challenge cannot be created,
 * configured, initialized, or executed correctly.
 * <p>
 * This exception represents failures related to the
 * lifecycle of a VulnaRebel challenge, such as invalid
 * configuration or initialization errors.
 * </p>
 */
public final class ChallengeException extends RuntimeException {
    public ChallengeException(String message) {
        super(message);
    }

    public ChallengeException(String message, Throwable cause) {
        super(message, cause);
    }
}
