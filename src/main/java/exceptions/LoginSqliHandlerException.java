package exceptions;

/**
 * Thrown when the Login SQL Injection handler
 * cannot process an incoming HTTP request.
 * <p>
 * Typical causes include invalid request processing,
 * response generation failures, or service initialization
 * errors delegated from the challenge service.
 * </p>
 */
public final class LoginSqliHandlerException extends RuntimeException{
    public LoginSqliHandlerException(String message){
        super(message);
    }

    public LoginSqliHandlerException(String message, Throwable cause){
        super(message,cause);
    }
}
