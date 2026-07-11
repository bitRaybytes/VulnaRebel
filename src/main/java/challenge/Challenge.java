package challenge;

import config.Configuration;
import exceptions.ChallengeException;
import exceptions.SchemaInitializerException;
import http.Route;

public abstract class Challenge {
    private final Configuration config;

    protected Challenge(Configuration config) throws ChallengeException {
        if (config == null){
            throw new ChallengeException(
                    getClass().getName() +
                            ": Configuration file cannot be null."
            );
        }
        this.config = config;
    }

    public Configuration config(){
        return config;
    }

    public abstract Route route();

    // intentionally empty — not all challenges require database initialization
    public void initialize() throws SchemaInitializerException {
    }
}
