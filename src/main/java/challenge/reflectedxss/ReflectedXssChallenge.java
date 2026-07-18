package challenge.reflectedxss;

import article.ArticleCard;
import article.ResourceHandler;
import challenge.Challenge;
import config.Configuration;
import config.ConfigurationLoader;
import exceptions.ChallengeException;
import html.TemplateRenderer;
import http.Route;

import java.util.List;
import java.util.Optional;

/**
 * Challenge module for the Reflected Xss topic.
 * <p>
 * Extends {@link Challenge}. Does not rely on any database connection, since this challenge is a client-side vulnerability.
 * </p>
 */
public class ReflectedXssChallenge extends Challenge {
    private final Configuration articleConfig;


    public ReflectedXssChallenge() throws ChallengeException {
        super(ConfigurationLoader.load("challenges/reflectedxss/challenge.properties"));
        this.articleConfig = ConfigurationLoader.load("challenges/reflectedxss/article.properties");
    }

    @Override
    public List<Route> routes() {
        TemplateRenderer renderer = new TemplateRenderer(articleConfig);
        return List.of(new Route(
                config().getString("challenge.route"), new ReflectedXssHandler()),
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
}
