package exceptions;

/**
 * Thrown when the challenge's handler fails during startup
 * or while initializing core components.
 */
public class ChallengesHandlerException extends RuntimeException {
    public ChallengesHandlerException(String message) {
        super(message);
    }

    public ChallengesHandlerException(String message, Throwable cause){
        super(message, cause);
    }
}
