package challenge.storedxss;

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

/**
 * Challenge module for the Stored Cross-Site Scripting (Stored XSS) topic.
 * <p>
 * Registers the challenge endpoint together with its corresponding
 * resource article and initializes the required database schema.
 * </p>
 *
 * <p>
 * Unlike the Reflected XSS challenge, user input is persisted inside
 * the database before being rendered again. Because the stored content
 * is intentionally written back into the HTML response without
 * sanitization, malicious JavaScript executes whenever the page is
 * revisited.
 * </p>
 *
 * <p>
 * This module also exposes a knowledge-base article explaining the
 * vulnerability and its mitigation techniques.
 * </p>
 */
public class StoredXssChallenge extends Challenge {

    private final DatabaseManager manager;
    private final Configuration articleConfig;

    public StoredXssChallenge(DatabaseManager manager) {
        super(ConfigurationLoader.load("challenges/storedxss/challenge.properties"));
        this.manager = manager;
        this.articleConfig = ConfigurationLoader.load("challenges/storedxss/article.properties");
    }

    @Override
    public List<Route> routes() {
        StoredXssService service = new StoredXssService(manager);
        StoredXssHandler getHandler = new StoredXssHandler(service,config());
        TemplateRenderer renderer = new TemplateRenderer(articleConfig);

        return List.of(
                new Route(config().getString("challenge.route"), getHandler),
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
        new SchemaInitializer(manager, config())
                .initialize(config().getString("challenge.initialize"));
    }
}
