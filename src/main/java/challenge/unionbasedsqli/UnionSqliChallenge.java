package challenge.unionbasedsqli;

import article.ArticleCard;
import article.ResourceHandler;
import challenge.Challenge;
import challenge.ChallengeLink;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import database.SchemaInitializer;
import exceptions.ChallengeException;
import html.TemplateRenderer;
import http.Route;

import java.util.List;
import java.util.Optional;

/**
 * Challenge module for the UNION-based SQL Injection challenge.
 * <p>
 * Registers the vulnerable search endpoint together with its
 * accompanying resource article and initializes the database
 * schema required for the challenge.
 * </p>
 * <p>
 * The challenge demonstrates how attackers can manipulate SQL
 * queries using the {@code UNION SELECT} operator to retrieve
 * information from unintended database tables.
 * </p>
 */
public class UnionSqliChallenge extends Challenge {
    private final DatabaseManager manager;
    private final Configuration articleConfig;

    public UnionSqliChallenge(DatabaseManager manager) {
        super(ConfigurationLoader.load("challenges/unionbasedsqli/challenge.properties"));
        this.manager = manager;
        this.articleConfig = ConfigurationLoader.load("challenges/unionbasedsqli/article.properties");
    }

    @Override
    public List<Route> routes() {
        UnionSqliService service = new UnionSqliService(manager);
        TemplateRenderer renderer= new TemplateRenderer(articleConfig);
        return List.of(
                new Route(config().getString("challenge.route"),   new UnionSqliHandler(service,config())),
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
    public void initialize() throws ChallengeException {
        new SchemaInitializer(manager)
                .initialize(config().getString("challenge.initialize"));
    }

    @Override
    public Optional<ChallengeLink> challengeLink() {
        return Optional.of(new ChallengeLink(
                config().getString("challenge.route"),
                config().getString("challenge.name"),
                config().getString("challenge.difficulty")
        ));
    }

}
