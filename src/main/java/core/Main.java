package core;

import article.ArticleCard;
import article.ResourceIndexHandler;
import challenge.Challenge;
import challenge.blindsqli.BlindSqliChallenge;
import challenge.loginsqli.LoginSqliChallenge;
import challenge.reconnaissance.ReconnaissanceChallenge;
import challenge.reflectedxss.ReflectedXssChallenge;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import http.ChallengesHandler;
import http.Route;
import http.Router;
import http.VulnaHttpServer;

import java.util.List;
import java.util.Optional;


public class Main {
    static void main(String[] args) throws Exception {
        // config
        Configuration appConfig = ConfigurationLoader.load("application.properties");

        // database
        DatabaseManager dbManager = new DatabaseManager(appConfig);

        // challenges
        List<Challenge> challenges = List.of(
            new ReconnaissanceChallenge(),
            new LoginSqliChallenge(dbManager),
            new ReflectedXssChallenge(),
            new BlindSqliChallenge(dbManager)
        );

        // initialize challenge schemas before registering a router
        for (Challenge challenge : challenges){
            challenge.initialize();
        }

        List<ArticleCard> cards = challenges.stream()
                .map(Challenge::articleCard)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // Router
        Router router = new Router();
        router.register(new Route(
                appConfig.getString("application.challengesRoute"), new ChallengesHandler()));
        router.register(new Route("/resources", new ResourceIndexHandler(cards)));
        for (Challenge challenge : challenges){
            for (Route route : challenge.routes()){
                router.register(route);

            }
        }

        // server & application
        new Application(new VulnaHttpServer(appConfig), router).start();
    }
}
