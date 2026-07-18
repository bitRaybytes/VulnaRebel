package http;

import exceptions.RouterException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {
    @Test
    void constructor_shouldCreateEmptyRouter() {

        Router router = new Router();

        assertTrue(router.getRoutes().isEmpty());
    }

    @Test
    void register_shouldAddRoute() {

        Router router = new Router();

        Route route =
                new Route("/login", new DummyHandler());

        router.register(route);

        assertEquals(1, router.getRoutes().size());
        assertEquals(route, router.getRoutes().getFirst());
    }

    @Test
    void register_shouldThrow_whenRouteIsNull() {

        Router router = new Router();

        assertThrows(
                RouterException.class,
                () -> router.register(null)
        );
    }

    @Test
    void register_shouldThrow_whenRouteAlreadyExists() {

        Router router = new Router();

        router.register(
                new Route("/login", new DummyHandler())
        );

        assertThrows(
                RouterException.class,
                () -> router.register(
                        new Route("/login", new DummyHandler())
                )
        );
    }

    @Test
    void getRoutes_shouldReturnRegisteredRoutes() {

        Router router = new Router();

        Route login =
                new Route("/login", new DummyHandler());

        Route search =
                new Route("/search", new DummyHandler());

        router.register(login);
        router.register(search);

        List<Route> routes = router.getRoutes();

        assertEquals(2, routes.size());
        assertTrue(routes.contains(login));
        assertTrue(routes.contains(search));
    }

    @Test
    void getRoutes_shouldReturnImmutableList() {

        Router router = new Router();

        router.register(
                new Route("/login", new DummyHandler())
        );

        List<Route> routes = router.getRoutes();

        assertThrows(
                UnsupportedOperationException.class,
                () -> routes.add(
                        new Route("/x", new DummyHandler())
                )
        );
    }

}
