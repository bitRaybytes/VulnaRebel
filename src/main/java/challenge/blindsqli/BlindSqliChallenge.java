package challenge.blindsqli;

import article.ArticleCard;
import article.ResourceHandler;
import challenge.Challenge;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import database.SchemaInitializer;
import exceptions.ChallengeException;
import exceptions.SchemaInitializerException;
import html.TemplateRenderer;
import http.Route;

import java.util.List;
import java.util.Optional;

/**
 * Challenge module for the Blind SQL Injection topic.
 * <p>
 * Extends {@link Challenge} and overrides {@link #initialize()} to create
 * the {@code blind_users} table and seed it with dummy data.
 * Requires a {@link DatabaseManager} for database initialization
 * and per-request query execution.
 * </p>
 */
public class BlindSqliChallenge extends Challenge {
    private final DatabaseManager dbManager;
    private final Configuration articleConfig;

    /**
     * @param dbManager the database connection factory required
     *                  for schema initialization and query execution
     * @throws ChallengeException if either configuration file
     *                            cannot be loaded
     */
    public BlindSqliChallenge(DatabaseManager dbManager) throws ChallengeException {
        super(ConfigurationLoader.load("challenges/blindsqli/challenge.properties"));
        this.dbManager = dbManager;
        this.articleConfig = ConfigurationLoader.load("challenges/blindsqli/article.properties");
    }

    @Override
    public List<Route> routes() {
        BlindSqliService service = new BlindSqliService(dbManager);
        BlindSqliHandler handler = new BlindSqliHandler(service, config());
        BlindSqliSubmitHandler submitHandler = new BlindSqliSubmitHandler(config());
        TemplateRenderer renderer = new TemplateRenderer(articleConfig);
        return List.of(
                new Route(config().getString("challenge.route"), handler),
                new Route(config().getString("challenge.route.submit"), submitHandler),
                new Route(articleConfig.getString("card.routing"), new ResourceHandler(renderer))
        );
    }

    @Override
    public Optional<ArticleCard> articleCard() {
        return Optional.of(new ArticleCard(
                articleConfig.getString("card.title"),
                articleConfig.getString("card.description"),
                articleConfig.getString("card.routing")
        ));
    }

    @Override
    public void initialize() throws SchemaInitializerException {
        new SchemaInitializer( dbManager, config() ).initialize(config().getString("challenge.initialize"));
    }
}
