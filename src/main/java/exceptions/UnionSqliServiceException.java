package exceptions;

import challenge.unionbasedsqli.UnionSqliService;

/**
 * Thrown when the {@link UnionSqliService}
 * cannot execute or process a database operation.
 * <p>
 * Typical causes include SQL execution failures,
 * database communication errors, malformed queries,
 * or unexpected exceptions during challenge processing.
 * </p>
 */
public class UnionSqliServiceException extends RuntimeException {
    public UnionSqliServiceException(String message){
        super(message);
    }

    public UnionSqliServiceException(String message, Throwable cause) {
        super(message,cause);
    }
}
