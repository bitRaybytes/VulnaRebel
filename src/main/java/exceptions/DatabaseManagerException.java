package exceptions;

/**
 * Thrown when the {@code DatabaseManager} cannot establish,
 * maintain, or close a database connection.
 * <p>
 * This exception also represents failures while executing
 * connection-related database operations.
 * </p>
 */
public final class DatabaseManagerException extends RuntimeException{
    public DatabaseManagerException(String message, Throwable cause){
        super(message, cause);
    }
    public DatabaseManagerException(String message){
        super(message);
    }
}
