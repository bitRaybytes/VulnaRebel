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

    public void register(Route route) {
        if (route == null) {
            throw new RouterException(Router.class.getName() +
                    ": Route cannot be null.");
        }

        for (Route existing : routes) {
            if (existing.getPath().equals(route.getPath())) {
                throw new RouterException(Router.class.getName() +
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
