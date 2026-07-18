package challenge;

import article.ArticleCard;
import config.Configuration;
import exceptions.ChallengeException;
import http.Route;

import java.util.List;
import java.util.Optional;

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
     * Returns all {@link Route} objects this challenge registers
     * with the {@link http.Router}.
     *
     * @return a list of routes for this challenge
     */
    public abstract List<Route> routes();

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
     * @throws ChallengeException if schema or seed
     *         SQL execution fails
     */
    public void initialize() throws ChallengeException {
        // intentionally empty - not all challenges require database initialization
    }

    /**
     * Returns the {@link ArticleCard} for this challenge's resource article,
     * used to populate the resource index page.
     * <p>
     * The default implementation returns {@link Optional#empty()} -
     * challenges without a resource article do not need to override this.
     * Challenges that provide an article should override both this method
     * and include the article route in {@link #routes()}.
     * </p>
     *
     * @return an {@link Optional} containing the card, or empty if
     *         this challenge has no associated article
     */
    public Optional<ArticleCard> articleCard() {
        return Optional.empty();
    }
}
