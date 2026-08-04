package core;

import article.ArticleCard;
import article.ResourceIndexHandler;
import challenge.Challenge;
import challenge.challengesIndex.ChallengesHandler;
import challenge.blindsqli.BlindSqliChallenge;
import challenge.ChallengeLink;
import challenge.loginsqli.LoginSqliChallenge;
import challenge.reconnaissance.ReconnaissanceChallenge;
import challenge.reflectedxss.ReflectedXssChallenge;
import challenge.storedxss.StoredXssChallenge;
import challenge.unionbasedsqli.UnionSqliChallenge;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import http.Route;
import http.Router;
import http.VulnaHttpServer;
import logging.Loggers;
import logging.LoggingConfigurator;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class Main {
    public static void main(String[] args) throws Exception {
        // startup banner
        System.out.println("\n"+BANNER);

        // logging
        LoggingConfigurator.configure();
        Logger LOG = Loggers.get(Main.class);

        // for measuring startup time
        long startup = System.nanoTime();

        LOG.info("Starting VulnaRebel...");

        // config
        Configuration appConfig = ConfigurationLoader.load("application.properties");

        // database
        DatabaseManager dbManager = new DatabaseManager(appConfig);

        // challenges
        List<Challenge> challenges = List.of(
                new ReconnaissanceChallenge(),
                new LoginSqliChallenge(dbManager),
                new ReflectedXssChallenge(),
                new BlindSqliChallenge(dbManager),
                new StoredXssChallenge(dbManager),
                new UnionSqliChallenge(dbManager)
        );

        LOG.info("Loaded " + challenges.size() + " challenges");

        LOG.info(()-> "Initializing " + challenges.size() + " challenges.");

        // initialize challenge schemas before registering a router
        for (Challenge challenge : challenges){
            challenge.initialize();
        }

        LOG.info("All challenges initialized.");

        List<ArticleCard> cards = challenges.stream()
                .map(Challenge::articleCard)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<ChallengeLink> links = challenges.stream()
                .map(Challenge::challengeLink)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // Router
        Router router = new Router();
        router.register(new Route(
                appConfig.getString("application.challengesRoute"), new ChallengesHandler(links)));
        router.register(new Route(
                appConfig.getString("application.resourcesRoute"), new ResourceIndexHandler(cards)));
        for (Challenge challenge : challenges){
            for (Route route : challenge.routes()){
                router.register(route);
            }
        }

        LOG.info(() -> "Registered " + router.getRoutes().size() + " routes.");

        // server & application
        new Application(new VulnaHttpServer(appConfig), router).start();

        // end of startup
        long elapsed = (System.nanoTime() - startup) / 1_000_000;

        LOG.info(()-> "Application bootstrap completed in "+ elapsed + " ms.");
    }

    private static final String BANNER =
            """
                  __      __    _              _____      _          _
                  \\ \\    / /   | |            |  __ \\    | |        | |
                   \\ \\  / /   _| |_ __   __ _ | |__) |___| |__   ___| |
                    \\ \\/ / | | | | '_ \\ / _` ||  _  // _ \\ '_ \\ / _ \\ |
                     \\  /| |_| | | | | | (_| || | \\ \\  __/ |_) |  __/ |
                      \\/  \\__,_|_|_| |_|\\__,_||_|  \\_\\___|_.__/ \\___|_|
                              Vulnerable Web Security Lab
                                      Version 1.0.0
            """;
}
