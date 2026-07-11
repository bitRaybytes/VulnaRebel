package challenge.reflectedxss;

import challenge.Challenge;
import config.Configuration;
import exceptions.ChallengeException;
import http.Route;

public class ReflectedXssChallenge extends Challenge {
    public ReflectedXssChallenge(Configuration config) throws ChallengeException {
        super(config);
    }

    @Override
    public Route route() {
        return new Route(config().getString("challenge.route"), new ReflectedXssHandler());
    }
}
