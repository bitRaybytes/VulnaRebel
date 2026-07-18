package exceptions;

/**
 * Thrown when the application's index page cannot
 * be generated or served successfully.
 * <p>
 * Typical causes include missing HTML resources or
 * failures while rendering the landing page.
 * </p>
 */
public final class IndexHandlerException extends RuntimeException {
    public IndexHandlerException(String message) {
        super(message);
    }

    public IndexHandlerException(String message, Throwable cause){
        super(message,cause);
    }
}
