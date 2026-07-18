package challenge.loginsqli;

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
 * Challenge module for the SQL Injection topic.
 * <p>
 * Extends {@link Challenge} and overrides {@link #initialize()} to create
 * the {@code login_users} table and seed it with dummy data.
 * Requires a {@link DatabaseManager} for database initialization
 * and per-request query execution.
 * </p>
 */
public class LoginSqliChallenge extends Challenge {
    private final DatabaseManager dbManager;
    private final Configuration articleConfig;

    /**
     * Challenge module for the SQL Injection login challenge.
     * <p>
     * Loads its own {@link Configuration} from
     * {@code challenges/loginsqli/challenge.properties} and
     * {@code challenges/loginsqli/article.properties} internally -
     * no configuration needs to be passed from {@link core.Main}.
     * </p>
     *
     * @param dbManager the database connection factory required
     *                  for schema initialization and query execution.
     * @throws ChallengeException if either configuration file
     *                            cannot be loaded.
     */
    public LoginSqliChallenge( DatabaseManager dbManager) throws ChallengeException {
        super(ConfigurationLoader.load("challenges/loginsqli/challenge.properties"));
        this.dbManager = dbManager;
        this.articleConfig = ConfigurationLoader.load("challenges/loginsqli/article.properties");
    }

    @Override
    public List<Route> routes() {
        LoginSqliService service = new LoginSqliService(dbManager);
        LoginSqliHandler handler = new LoginSqliHandler(service, config());
        TemplateRenderer renderer = new TemplateRenderer(articleConfig);
        return List.of(
                new Route(config().getString("challenge.route"), handler),
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
        new SchemaInitializer(dbManager,
                config()).initialize(config().getString("challenge.initialize"));
    }
}
