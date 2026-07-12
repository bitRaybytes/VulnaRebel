package challenge;

import config.Configuration;
import exceptions.ChallengeException;
import exceptions.SchemaInitializerException;
import http.Route;

/**
 * Base abstraction for all VulnaRebel challenge modules.
 * <p>
 * Each challenge is self-contained - it owns its HTTP route,
 * its handler, and optionally its database schema setup.
 * </p>
 * A {@link Configuration} loaded from the challenge's own
 * {@code challenge.properties} file is required.
 */
public abstract class Challenge {
    private final Configuration config;

    /**
     * @param config the challenge-specific configuration -
     *               must not be null
     * @throws ChallengeException if config is null
     */
    protected Challenge(Configuration config) throws ChallengeException {
        if (config == null){
            throw new ChallengeException(
                    getClass().getName() +
                            ": Configuration file cannot be null."
            );
        }
        this.config = config;
    }

    /**
     * Returns the challenge-specific configuration.
     *
     * @return the {@link Configuration} provided at construction
     */
    public Configuration config(){
        return config;
    }

    /**
     * Returns the {@link Route} that registers this challenge
     * with the {@link http.Router}.
     * <p>
     * Implementations must construct the handler and return
     * a fully wired {@link Route} with the challenge's path.
     * </p>
     *
     * @return the route for this challenge
     */
    public abstract Route route();

    /**
     * Initializes the database schema and seed data for this challenge.
     * <p>
     * May be overridden by challenges that require database tables -
     * for example {@link challenge.loginsqli.LoginSqliChallenge}.
     * Challenges with no database dependency, such as
     * {@link challenge.reflectedxss.ReflectedXssChallenge},
     * rely on this default empty implementation.
     * </p>
     *
     * @throws SchemaInitializerException if schema or seed
     *         SQL execution fails
     */
    public void initialize() throws SchemaInitializerException {
        // intentionally empty — not all challenges require database initialization
    }
}
