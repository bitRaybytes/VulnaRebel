package exceptions;

/**
 * Thrown when a {@link http.Route} cannot be created
 * or contains invalid routing information.
 * <p>
 * Typical causes include missing route paths,
 * invalid HTTP handlers, or malformed route definitions.
 * </p>
 */
public final class RouteException extends RuntimeException{
    public RouteException(String message){
        super(message);
    }

    public RouteException(String message, Throwable cause){
        super(message, cause);
    }

}
