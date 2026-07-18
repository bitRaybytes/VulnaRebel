package exceptions;

/**
 * Thrown when a resource article cannot
 * be rendered or served successfully.
 * <p>
 * Typical causes include missing article templates,
 * invalid article configurations, or rendering failures.
 * </p>
 */
public final class ResourceHandlerException extends RuntimeException {
    public ResourceHandlerException(String message) {
        super(message);
    }

    public ResourceHandlerException(String message, Throwable cause){
        super(message,cause);
    }
}
