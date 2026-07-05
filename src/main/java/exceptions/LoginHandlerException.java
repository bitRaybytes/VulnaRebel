package exceptions;

import java.sql.SQLException;

public class LoginHandlerException extends RuntimeException{
    public LoginHandlerException(String message){
        super(message);
    }

    public LoginHandlerException(String message, InterruptedException e){
        super(message,e);
    }

    public LoginHandlerException(String message, LoginServiceException e) {
        super(message,e);
    }
}
