package challenge.unionbasedsqli;

import article.ArticleCard;
import article.ResourceHandler;
import challenge.Challenge;
import config.Configuration;
import config.ConfigurationLoader;
import database.DatabaseManager;
import database.SchemaInitializer;
import exceptions.ChallengeException;
import html.TemplateRenderer;
import http.Route;

import java.util.List;
import java.util.Optional;

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
}
