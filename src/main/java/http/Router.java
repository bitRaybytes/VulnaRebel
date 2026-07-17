package http;

import exceptions.RouterException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class owns a collection of {@link Route} objects and registers them.
 */
public class Router {
    // TODO Return a Map<String,Route>, provides much better duplicate handling and lookup
    private final List<Route> routes;

    public Router(){
        this.routes = new ArrayList<>();

    }

    /**
     * To register a {@link Route} in {@code routes}
     * @param route - a {@code route} of {@link Route} object will get add to the list.
     * @throws RouterException if a {@code route} is null
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

    /**
     * A list of immutable {@link Route} objects.
     * @return an immutable {@link List} of {@link Route} objects.
     */
    public List<Route> getRoutes(){
        return List.copyOf(routes);
    }

}
