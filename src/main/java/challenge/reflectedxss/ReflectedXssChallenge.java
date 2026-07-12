package challenge.reflectedxss;

import challenge.Challenge;
import config.Configuration;
import exceptions.ChallengeException;
import http.Route;

/**
 * Challenge module for the Reflected Xss topic.
 * <p>
 * Extends {@link Challenge}. Does not rely on any database connection, since this challenge is a client-side vulnerability.
 * </p>
 */
public class ReflectedXssChallenge extends Challenge {
    /**
     * @param config    the challenge-specific configuration
     * @throws ChallengeException if {@code config} is null
     */
    public ReflectedXssChallenge(Configuration config) throws ChallengeException {
        super(config);
    }

    @Override
    public Route route() {
        return new Route(config().getString("challenge.route"), new ReflectedXssHandler());
    }
}
