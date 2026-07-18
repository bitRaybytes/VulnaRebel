package challenge.reconnaissance;

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
 * Challenge module for the Reconnaissance topic.
 * <p>
 * Extends {@link Challenge}. Serves the initial welcome page at {@code /},
 * validates the flag submission at {@code /flag}, and provides a
 * reconnaissance article at its configured resource route.
 * </p>
 * <p>
 * No database connection is required - this challenge is purely
 * HTML and config based.
 * </p>
 * A {@code challenge.properties} and {@code article.properties} are
 * loaded internally - no configuration needs to be passed from
 * {@link core.Main}.
 */
public class ReconnaissanceChallenge extends Challenge {
    private final Configuration articleConfig;

    /**
     * @throws ChallengeException if either configuration file
     *                            cannot be loaded
     */
    public ReconnaissanceChallenge() throws ChallengeException {
        super(ConfigurationLoader.load("challenges/reconnaissance/challenge.properties"));
        this.articleConfig = ConfigurationLoader.load("challenges/reconnaissance/article.properties");
    }

    @Override
    public List<Route> routes() {
        ReconnaissanceSubmitHandler submitHandler = new ReconnaissanceSubmitHandler(config());
        TemplateRenderer renderer = new TemplateRenderer(articleConfig);
        return List.of(
                new Route(config().getString("challenge.route"), new ReconnaissanceHandler()),
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
}
