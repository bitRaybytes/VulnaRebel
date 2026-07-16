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
        Configuration appConfig          = ConfigurationLoader.load("application.properties");
        Configuration loginSqliConfig    = ConfigurationLoader.load("challenges/loginsqli/challenge.properties");
        Configuration reflectedXssConfig = ConfigurationLoader.load("challenges/reflectedxss/challenge.properties");
        Configuration blindsqliConfig    = ConfigurationLoader.load("challenges/blindsqli/challenge.properties");

        // database
        DatabaseManager dbManager = new DatabaseManager(appConfig);

        // challenges
        Challenge loginSqli     = new LoginSqliChallenge(loginSqliConfig,dbManager);
        Challenge reflextedXss  = new ReflectedXssChallenge(reflectedXssConfig);
        Challenge blindSqli     = new BlindSqliChallenge(blindsqliConfig, dbManager);


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
        router.register(new Route("/", new IndexHandler()));
        router.register(loginSqli.route());
        router.register(reflextedXss.route());
        //registers blindSqli Routes:
        for (Route route : blindSqli.routes()){
            router.register(route);
        }
        router.register(new Route("/resources", new ResourceIndexHandler(articles)));
        router.register(new Route("/resources/sql-injection", sqliArticle));
        router.register(new Route("/resources/blind-sql-injection", blindSqliArticle));
        router.register(new Route("/resources/reflected-xss", reflectedXssArticle));

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
