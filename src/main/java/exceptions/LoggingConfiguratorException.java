package exceptions;

public class LoggingConfiguratorException extends RuntimeException {
    public LoggingConfiguratorException(String message) {
        super(message);
    }

    public LoggingConfiguratorException(String message, Throwable cause){
        super(message, cause);
    }
}
