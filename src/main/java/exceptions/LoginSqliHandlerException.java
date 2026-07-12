package exceptions;

public class LoginSqliHandlerException extends RuntimeException{
    public LoginSqliHandlerException(String message){
        super(message);
    }

    public LoginSqliHandlerException(String message, InterruptedException e){
        super(message,e);
    }

    public LoginSqliHandlerException(String message, LoginSqliServiceException e) {
        super(message,e);
    }
}
