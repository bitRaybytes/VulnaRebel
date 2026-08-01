package http;

import exceptions.RouteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    private final DummyHandler dummyHandler = new DummyHandler();

    @Nested
    @DisplayName("Constructor guards — path")
    class PathGuards {

        @Test
        @DisplayName("throws when path is null")
        void throwsOnNullPath() {
            assertThrows(RouteException.class,
                    () -> new Route(null, dummyHandler));
        }

        @Test
        @DisplayName("throws when path is blank")
        void throwsOnBlankPath() {
            assertThrows(RouteException.class,
                    () -> new Route("   ", dummyHandler));
        }

        @Test
        @DisplayName("throws when path is empty string")
        void throwsOnEmptyPath() {
            assertThrows(RouteException.class,
                    () -> new Route("", dummyHandler));
        }

        @Test
        @DisplayName("throws when path does not start with slash")
        void throwsWhenPathMissingLeadingSlash() {
            assertThrows(RouteException.class,
                    () -> new Route("loginsqli", dummyHandler));
        }
    }

    @Nested
    @DisplayName("Constructor guards — handler")
    class HandlerGuards {

        @Test
        @DisplayName("throws when handler is null")
        void throwsOnNullHandler() {
            assertThrows(RouteException.class,
                    () -> new Route("/test", null));
        }
    }

    @Nested
    @DisplayName("Construction succeeds")
    class ConstructionSucceeds {

        @Test
        @DisplayName("constructs with valid path and handler")
        void constructsWithValidArgs() {
            assertDoesNotThrow(() -> new Route("/loginsqli", dummyHandler));
        }

        @Test
        @DisplayName("constructs with root path")
        void constructsWithRootPath() {
            assertDoesNotThrow(() -> new Route("/", dummyHandler));
        }

        @Test
        @DisplayName("constructs with nested path")
        void constructsWithNestedPath() {
            assertDoesNotThrow(
                    () -> new Route("/resources/sql-injection", dummyHandler));
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        @DisplayName("getPath returns the path provided at construction")
        void getPathReturnsConstructedPath() {
            Route route = new Route("/loginsqli", dummyHandler);
            assertEquals("/loginsqli", route.getPath());
        }

        @Test
        @DisplayName("getHandler returns the handler provided at construction")
        void getHandlerReturnsConstructedHandler() {
            Route route = new Route("/loginsqli", dummyHandler);
            assertSame(dummyHandler, route.getHandler());
        }

        @Test
        @DisplayName("getPath and getHandler return consistent values")
        void pathAndHandlerAreConsistent() {
            Route route = new Route("/blind-sqli", dummyHandler);

            assertEquals("/blind-sqli", route.getPath());
            assertSame(dummyHandler, route.getHandler());
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("path does not change between calls")
        void pathIsStable() {
            Route route = new Route("/loginsqli", dummyHandler);
            String first  = route.getPath();
            String second = route.getPath();
            assertEquals(first, second);
        }

        @Test
        @DisplayName("handler does not change between calls")
        void handlerIsStable() {
            Route route = new Route("/loginsqli", dummyHandler);
            assertSame(route.getHandler(), route.getHandler());
        }
    }
}
