package logging;

import java.util.logging.Logger;

/**
 * Utility class for obtaining {@link Logger} instances.
 * <p>
 * Each class that performs logging can obtain its dedicated
 * {@link Logger} by passing its {@link Class} object to
 * {@link #get(Class)}.
 * </p>
 * <p>Example:</p>
 * <pre>{@code
 *      private static final Logger LOGGER = Loggers.get(DatabaseManager.class);
 * }</pre>
 */
public final class Loggers {

    private Loggers (){
        throw new AssertionError("Utility class");
    }

    /**
     * Returns the {@link Logger} associated with the given class.
     *
     * @param clazz the class requesting a logger
     * @return the logger associated with the given class
     */
    public static Logger get(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
