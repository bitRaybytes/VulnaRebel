package core;

import challenge.Challenge;
import challenge.loginsqli.LoginSqliChallenge;
import challenge.reflectedxss.ReflectedXssChallenge;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import exceptions.ChallengeException;
import http.IndexHandler;
import http.Route;
import http.Router;
import http.VulnaHttpServer;


public class Main {
    public static void main(String[] args) throws Exception, ChallengeException {
        // configs
        Configuration appConfig          = ConfigurationLoader.load("application.properties");
        Configuration loginSqliConfig    = ConfigurationLoader.load("challenges/login/challenge.properties");
        Configuration reflectedXssConfig = ConfigurationLoader.load("challenges/reflectedxss/challenge.properties");

        // database
        DatabaseManager dbManager = new DatabaseManager(appConfig);

        // challenges
        Challenge loginSqli     = new LoginSqliChallenge(loginSqliConfig,dbManager);
        Challenge reflextedXss  = new ReflectedXssChallenge(reflectedXssConfig);

        // Router
        Router router = new Router();
        router.register(new Route("/", new IndexHandler()));
        router.register(loginSqli.route());
        router.register(reflextedXss.route());

        // server
        VulnaHttpServer server = new VulnaHttpServer(appConfig);

        // application
        new Application(server, router).start();
    }
}
