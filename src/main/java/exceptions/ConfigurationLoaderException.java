package exceptions;

/**
 * Thrown when a configuration file cannot be loaded
 * or parsed successfully.
 */
public final class ConfigurationLoaderException extends RuntimeException {
    public ConfigurationLoaderException(String message){
        super(message);
    }

    public ConfigurationLoaderException(String message, Throwable cause) {
        super(message,cause);
    }
}
