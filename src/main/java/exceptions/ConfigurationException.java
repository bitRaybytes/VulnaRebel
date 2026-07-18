package exceptions;

/**
 * Thrown when an invalid configuration value is accessed
 * or when a configuration cannot satisfy the expected contract.
 * <p>
 * Typical causes include missing properties, invalid data types,
 * malformed values, or empty configuration files.
 * </p>
 */
public final class ConfigurationException extends RuntimeException{
    public ConfigurationException(String message){
        super(message);
    }

    public ConfigurationException(String message, Throwable cause){
        super(message, cause);
    }
}
