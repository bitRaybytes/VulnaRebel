package challenge.blindsqli;

import challenge.Challenge;
import config.Configuration;
import database.DatabaseManager;
import database.SchemaInitializer;
import exceptions.ChallengeException;
import exceptions.SchemaInitializerException;
import http.Route;

import java.util.List;

/**
 * Challenge module for the Blind SQL Injection topic.
 * <p>
 * Extends {@link Challenge} and overrides {@link #initialize()} to create
 * the {@code blind_users} table and seed it with dummy data.<br>
 * Requires a {@link DatabaseManager} for database initialization
 * and per-request query execution.
 * </p>
 */
public class BlindSqliChallenge extends Challenge {
    private final DatabaseManager dbManager;

    /**
     * @param config    the challenge-specific configuration
     * @param dbManager the database connection factory,
     *                  required for schema initialization
     *                  and query execution
     * @throws ChallengeException if {@code config} is null
     */
    public BlindSqliChallenge(Configuration config, DatabaseManager dbManager) throws ChallengeException {
        super(config);
        this.dbManager = dbManager;
    }

    @Override
    public Route route() {
        return null;
    }

    @Override
    public List<Route> routes() {
        BlindSqliService service = new BlindSqliService(dbManager);
        BlindSqliHandler handler = new BlindSqliHandler(service, config());
        BlindSqliSubmitHandler submitHandler = new BlindSqliSubmitHandler(config());
        return List.of(
                new Route(config().getString("challenge.route"), handler),
                new Route(config().getString("challenge.route.submit"), submitHandler)
        );
    }

    @Override
    public void initialize() throws SchemaInitializerException {
        new SchemaInitializer( dbManager, config() ).initialize(config().getString("challenge.initialize"));
    }
}
