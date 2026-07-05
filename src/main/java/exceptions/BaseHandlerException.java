package exceptions;

import java.io.IOException;

public class BaseHandlerException extends RuntimeException{
    public BaseHandlerException(String message){
        super(message);
    }

    public BaseHandlerException(String message,IOException e){
        super(message,e);
    }



}
