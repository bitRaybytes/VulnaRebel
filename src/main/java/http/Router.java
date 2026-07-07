package http;

import exceptions.RouterException;

import java.util.ArrayList;
import java.util.List;

/// This class owns a collection of Route objects
public class Router {

    private final List<Route> routes;

    public Router(){
        this.routes = new ArrayList<>();

    }

    /**
     * To register a Route in {@code routes}
     * @param route - a route of Route object will get add to the list.
     * @throws RouterException
     */
    public void register(Route route) {
        if (route == null) {
            throw new RouterException(
                    getClass().getName() +
                            ": Route cannot be null.");
        }

        for (Route existing : routes) {
            if (existing.getPath().equals(route.getPath())) {
                throw new RouterException(
                        getClass().getName() +
                            ": Duplicate route detected: " + route.getPath()
                );
            }
        }

        routes.add(route);
    }

    public List<Route> getRoutes(){
        return List.copyOf(routes);
    }

}
