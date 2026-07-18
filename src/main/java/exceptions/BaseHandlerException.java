package exceptions;

import java.io.IOException;

/**
 * Thrown when a {@code BaseHandler} encounters an invalid state
 * while processing an HTTP request or response.
 * <p>
 * This includes invalid request data, missing resources,
 * malformed input, or failures while sending a response.
 * </p>
 */
public final class BaseHandlerException extends RuntimeException{
    public BaseHandlerException(String message){
        super(message);
    }

    public BaseHandlerException(String message,Throwable cause){
        super(message,cause);
    }



}
