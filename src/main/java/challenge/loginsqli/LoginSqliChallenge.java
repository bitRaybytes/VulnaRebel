package challenge.loginsqli;

import challenge.Challenge;
import config.Configuration;
import database.DatabaseManager;
import database.SchemaInitializer;
import exceptions.ChallengeException;
import exceptions.LoginSqliHandlerException;
import exceptions.LoginSqliServiceException;
import exceptions.SchemaInitializerException;
import http.Route;

/**
 * Challenge module: {@code LoginSqliChallenge}
 * Extends base class {@link Challenge} and overloads the {@code initialize()} method to set up a challenge related database table and dummy data.
 * {@link DatabaseManager} is required to initialize.
 */
public class LoginSqliChallenge extends Challenge {
    private final DatabaseManager dbManager;

    /**
     * A Challenge object related to the SQL-Injection topic.
     * @param config to load challenge specific configurations
     * @param dbManager for initializing the database table
     * @throws ChallengeException if an error occurs.
     */
    public LoginSqliChallenge(Configuration config, DatabaseManager dbManager) throws ChallengeException {
        super(config);
        this.dbManager = dbManager;
    }

    @Override
    public Route route() {
        LoginSqliService service = null;
        LoginSqliHandler handler = null;
        try {
            service = new LoginSqliService(dbManager);
            handler = new LoginSqliHandler(service, config());
        } catch (LoginSqliServiceException e) {
            throw new LoginSqliHandlerException(
                    getClass().getName() +
                            ": Failed to load LoginSqliService class.",e);
        }
        return new Route(config().getString("challenge.route"), handler);
    }

    @Override
    public void initialize() throws SchemaInitializerException {
        new SchemaInitializer(dbManager,
                config()).initialize(config().getString("challenge.initialize"));
    }
}
