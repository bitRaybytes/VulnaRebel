package logging;

import exceptions.LoggingConfiguratorException;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LoggingConfigurator {

    private LoggingConfigurator(){
        throw new AssertionError("Utility Class");
    }

    /**
     * Loads and applies the {@code logging.properties} configuration
     * from the classpath.
     *
     * @throws LoggingConfiguratorException if the configuration file cannot
     *              be found or an {@link IOException} occurs while reading it
     */
    public static void configure(){
        try (InputStream in =
                     LoggingConfigurator.class.getResourceAsStream("/logging.properties")
        ) {
            if (in == null){
                throw new LoggingConfiguratorException(
                        LoggingConfigurator.class.getName() +": Resource '/logging.properties' not found."
                );
            }

            LogManager.getLogManager().readConfiguration(in);

        } catch (IOException e) {
            throw new LoggingConfiguratorException(
                    LoggingConfigurator.class.getName() +
                            ": Failed to load logging configuration.", e
            );
        }
    }
}
