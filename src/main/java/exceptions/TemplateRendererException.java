package exceptions;

/**
 * Thrown when an HTML template cannot be rendered
 * successfully.
 * <p>
 * Typical causes include missing template placeholders,
 * invalid template resources, or rendering failures
 * during placeholder replacement.
 * </p>
 */
public final class TemplateRendererException extends RuntimeException {
    public TemplateRendererException(String message) {
        super(message);
    }

    public TemplateRendererException(String message, Throwable cause){
        super(message,cause);
    }
}
