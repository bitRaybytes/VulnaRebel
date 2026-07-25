package exceptions;

/**
 * Thrown when the {@link challenge.unionbasedsqli.UnionSqliHandler}
 * cannot process an incoming HTTP request.
 * <p>
 * Typical causes include invalid request processing,
 * response generation failures, or service initialization
 * errors delegated from the challenge service.
 * </p>
 */
public class UnionSqliHandlerException extends RuntimeException {
    public UnionSqliHandlerException(String message) {
        super(message);
    }

    public UnionSqliHandlerException(String message, Throwable cause){
        super(message,cause);
    }
}
