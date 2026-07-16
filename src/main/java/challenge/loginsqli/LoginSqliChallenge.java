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
 * Challenge module for the SQL Injection topic.
 * <p>
 * Extends {@link Challenge} and overrides {@link #initialize()} to create
 * the {@code login_users} table and seed it with dummy data.<br>
 * Requires a {@link DatabaseManager} for database initialization
 * and per-request query execution.
 * </p>
 */
public class LoginSqliChallenge extends Challenge {
    private final DatabaseManager dbManager;

    /**
     * @param config    the challenge-specific configuration
     * @param dbManager the database connection factory,
     *                  required for schema initialization
     *                  and query execution
     * @throws ChallengeException if {@code config} is null
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
    public void initialize() throws ChallengeException {
        new SchemaInitializer(dbManager,
                config()).initialize(config().getString("challenge.initialize"));
    }
}
