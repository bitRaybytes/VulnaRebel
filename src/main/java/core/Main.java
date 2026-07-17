package core;

import article.ArticleCard;
import article.ResourceHandler;
import article.ResourceIndexHandler;
import challenge.Challenge;
import challenge.blindsqli.BlindSqliChallenge;
import challenge.loginsqli.LoginSqliChallenge;
import challenge.reflectedxss.ReflectedXssChallenge;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import html.TemplateRenderer;
import http.IndexHandler;
import http.Route;
import http.Router;
import http.VulnaHttpServer;

import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        // configs
        Configuration appConfig = ConfigurationLoader.load("application.properties");

        // database
        DatabaseManager dbManager = new DatabaseManager(appConfig);

        // challenges
        List<Challenge> challenges = List.of(
            new LoginSqliChallenge(
                    ConfigurationLoader.load("challenges/loginsqli/challenge.properties"),
                    dbManager),
            new ReflectedXssChallenge(
                    ConfigurationLoader.load("challenges/reflectedxss/challenge.properties")),
            new BlindSqliChallenge(
                    ConfigurationLoader.load("challenges/blindsqli/challenge.properties"),
                    dbManager)
        );

        // initialize challenge schemas before registering a router
        for (Challenge challenge : challenges){
            challenge.initialize();
        }


        // TODO
        // A challenge should implement its own resources.

        // article configs
        TemplateRenderer sqliResource = new TemplateRenderer(
                ConfigurationLoader.load("challenges/loginsqli/article.properties")
        );
        TemplateRenderer blindSqliResource = new TemplateRenderer(
                ConfigurationLoader.load("challenges/blindsqli/article.properties")
        );
        TemplateRenderer reflectedXssResource = new TemplateRenderer(
                ConfigurationLoader.load("challenges/reflectedxss/article.properties")
        );
        // articles
        ResourceHandler sqliArticle = new ResourceHandler(sqliResource);
        ResourceHandler blindSqliArticle = new ResourceHandler(blindSqliResource);
        ResourceHandler reflectedXssArticle = new ResourceHandler(reflectedXssResource);

        // cards
        List<ArticleCard> articles = List.of(
                new ArticleCard("SQL Injection",
                        "Learn how unsanitized input reaches the database.",
                        "/resources/sql-injection"),
                new ArticleCard("Blind SQL Injection",
                        "Extract data character by character without visible output.",
                        "/resources/blind-sql-injection"),
                new ArticleCard("Reflected XSS",
                        "Understand how input is reflected back as executable script.",
                        "/resources/reflected-xss")
        );


        // Router
        Router router = new Router();
        router.register(new Route(appConfig.getString("application.indexRoute"), new IndexHandler()));
        for (Challenge challenge : challenges){
            router.register(challenge.route());
        }
        router.register(new Route("/resources", new ResourceIndexHandler(articles)));
        router.register(new Route("/resources/sql-injection", sqliArticle));
        router.register(new Route("/resources/blind-sql-injection", blindSqliArticle));
        router.register(new Route("/resources/reflected-xss", reflectedXssArticle));

        // server
        VulnaHttpServer server = new VulnaHttpServer(appConfig);

        // application
        new Application(server, router).start();
    }
}
