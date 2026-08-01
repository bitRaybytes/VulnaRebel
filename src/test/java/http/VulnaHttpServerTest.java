package http;

import config.Configuration;
import config.ConfigurationLoader;
import exceptions.VulnaHttpServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VulnaHttpServerTest {
    private Configuration testConfig() {
        return ConfigurationLoader.load("test-server.properties");
    }

    @Nested
    @DisplayName("Constructor guards")
    class ConstructorGuards {

        @Test
        @DisplayName("throws when config is null")
        void throwsOnNullConfig() {
            assertThrows(VulnaHttpServerException.class,
                    () -> new VulnaHttpServer(null));
        }

        @Test
        @DisplayName("constructs successfully with valid config")
        void constructsWithValidConfig() {
            assertDoesNotThrow(() -> new VulnaHttpServer(testConfig()));
        }
    }

    @Nested
    @DisplayName("addContext guards")
    class AddContextGuards {

        @Test
        @DisplayName("throws when path is null")
        void throwsOnNullPath() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            assertThrows(VulnaHttpServerException.class,
                    () -> server.addContext(null, exchange -> {}));
        }

        @Test
        @DisplayName("throws when path is blank")
        void throwsOnBlankPath() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            assertThrows(VulnaHttpServerException.class,
                    () -> server.addContext("   ", exchange -> {}));
        }

        @Test
        @DisplayName("throws when handler is null")
        void throwsOnNullHandler() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            assertThrows(VulnaHttpServerException.class,
                    () -> server.addContext("/test", null));
        }
    }

    @Nested
    @DisplayName("applyRoutes")
    class ApplyRoutes {

        @Test
        @DisplayName("throws when router is null")
        void throwsOnNullRouter() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            assertThrows(VulnaHttpServerException.class,
                    () -> server.applyRoutes(null));
        }

        @Test
        @DisplayName("registers all routes from router without throwing")
        void registersAllRoutes() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            Router router = new Router();
            router.register(new Route("/test", exchange -> {}));
            router.register(new Route("/other", exchange -> {}));

            assertDoesNotThrow(() -> server.applyRoutes(router));
        }

        @Test
        @DisplayName("empty router applies without throwing")
        void emptyRouterApplies() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            assertDoesNotThrow(() -> server.applyRoutes(new Router()));
        }
    }

    @Nested
    @DisplayName("Lifecycle")
    class Lifecycle {

        @Test
        @DisplayName("start and stop complete without throwing")
        void startAndStopCleanly() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            assertDoesNotThrow(() -> {
                server.start();
                server.stop();
            });
        }

        @Test
        @DisplayName("stop is safe to call before start")
        void stopBeforeStartIsSafe() throws IOException {
            VulnaHttpServer server = new VulnaHttpServer(testConfig());
            assertDoesNotThrow(server::stop);
        }
    }
}
