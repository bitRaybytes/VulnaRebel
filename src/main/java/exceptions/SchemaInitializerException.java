package exceptions;

/**
 * Thrown when a challenge database schema cannot be
 * created, initialized, or seeded successfully.
 * <p>
 * Typical causes include SQL execution failures,
 * missing schema resources, or invalid initialization
 * scripts.
 * </p>
 */
public final class SchemaInitializerException extends RuntimeException {
    public SchemaInitializerException(String message) {
        super(message);
    }

    public SchemaInitializerException(String message, Throwable cause) {
        super(message, cause);
    }

}
