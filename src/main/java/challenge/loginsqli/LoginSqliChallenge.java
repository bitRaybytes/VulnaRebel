package challenge.loginsqli;

import challenge.Challenge;
import config.Configuration;
import database.DatabaseManager;
import database.SchemaInitializer;
import exceptions.ChallengeException;
import exceptions.LoginHandlerException;
import exceptions.LoginServiceException;
import exceptions.SchemaInitializerException;
import http.Route;

public class LoginSqliChallenge extends Challenge {
    private final DatabaseManager dbManager;

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
        } catch (LoginServiceException e) {
            throw new LoginHandlerException(
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
