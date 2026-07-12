package core;

import challenge.Challenge;
import challenge.blindsqli.BlindSqliChallenge;
import challenge.loginsqli.LoginSqliChallenge;
import challenge.reflectedxss.ReflectedXssChallenge;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import exceptions.ChallengeException;
import exceptions.SchemaInitializerException;
import http.IndexHandler;
import http.Route;
import http.Router;
import http.VulnaHttpServer;


public class Main {
    public static void main(String[] args) throws Exception, ChallengeException, SchemaInitializerException {
        // configs
        Configuration appConfig          = ConfigurationLoader.load("application.properties");
        Configuration loginSqliConfig    = ConfigurationLoader.load("challenges/login/challenge.properties");
        Configuration reflectedXssConfig = ConfigurationLoader.load("challenges/reflectedxss/challenge.properties");
        Configuration blindsqliConfig    = ConfigurationLoader.load("challenges/blindsqli/challenge.properties");

        // database
        DatabaseManager dbManager = new DatabaseManager(appConfig);

        // challenges
        Challenge loginSqli     = new LoginSqliChallenge(loginSqliConfig,dbManager);
        Challenge reflextedXss  = new ReflectedXssChallenge(reflectedXssConfig);
        Challenge blindSqli     = new BlindSqliChallenge(blindsqliConfig, dbManager);

        // Router
        Router router = new Router();
        router.register(new Route("/", new IndexHandler()));
        router.register(loginSqli.route());
        router.register(reflextedXss.route());
        //registers blindSqli Routes:
        for (Route route : blindSqli.routes()){
            router.register(route);
        }

        // initialize challenge schemas
        loginSqli.initialize();
        blindSqli.initialize();
        // reflectedXss.initialize() not needed — no database

        // server
        VulnaHttpServer server = new VulnaHttpServer(appConfig);

        // application
        new Application(server, router).start();
    }
}
