package http;

import com.sun.net.httpserver.HttpHandler;
import exceptions.RouteException;

/// This class represents a single path-to-handler mapping.</br>

public class Route {
    private final HttpHandler handler;
    private final String path;


    /// To implement a route, the constructor has to be configured with a path and a HttpHandler
    /// @param path is the path of the resource.
    /// @param handler is the HttpHandler which resolve the outcome.
    public Route(String path, HttpHandler handler) {
        validateHandler(handler);
        validatePath(path);
        this.path = path;
        this.handler = handler;
    }

    public HttpHandler getHandler() {
        return handler;
    }

    public String getPath() {
        return path;
    }

    private void validateHandler(HttpHandler handler){
        if (handler == null){
            throw new RouteException(
                    Route.class.getName() +
                            ": HttpHandler cannot be null.");
        }
    }

    private void validatePath(String path){
        if (path == null){
            throw new RouteException(
                    Route.class.getName() +
                            ": Path cannot be null.");
        }
        if (path.isBlank()){
            throw new RouteException(
                    Route.class.getName() +
                            ": Path cannot be empty.");
        }
    }
}
