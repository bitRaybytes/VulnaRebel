package exceptions;

public class ReflectedXssHandlerException extends RuntimeException {
    public ReflectedXssHandlerException(String message) {
        super(message);
    }
}
