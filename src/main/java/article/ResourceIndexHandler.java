package article;

import com.sun.net.httpserver.HttpExchange;
import http.BaseHandler;

import java.io.IOException;
import java.util.List;

/**
 * Serves the resource article index page listing all available
 * learning resources.
 * <p>
 * Handles {@code GET /resources} and renders {@code resources.html}
 * as a card grid. Each {@link ArticleCard} in the provided list
 * is rendered as an HTML card and injected into the
 * {@code {{cards}}} placeholder.
 * </p>
 *
 * @see ArticleCard
 */
public class ResourceIndexHandler extends BaseHandler {

    private final List<ArticleCard> cards;

    /**
     * @param cards the list of article cards to render on the index page -
     *              each card links to one resource article
     * @throws IllegalArgumentException if {@code cards} is null
     */
    public ResourceIndexHandler(List<ArticleCard> cards) {
        this.cards = cards;
    }

    /**
     * Serves the resource index page.
     *
     * @param exchange the HTTP exchange for this request
     * @throws IOException if the HTML resource cannot be read
     *                     or the response cannot be written
     */
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/resources.html");
        String replaced = new String(html).replace("{{cards}}", buildCards());
        sendResponse(exchange,200,TEXT_HTML,replaced);
    }

    /**
     * Creates the card layout for the resource index page each containing a title,
     * a description and a route to a challenge-specific topic.
     * @return A String with html code to replace {@code {{cards}}} keywords in {@code resources.html}.
     */
    private String buildCards() {
        StringBuilder sb = new StringBuilder();
        for (ArticleCard card : cards) {
            sb.append("<div style=\"display:flex; flex-direction:column; align-items:center; ")
                    .append("width:200px; background-color:grey; padding:10px; ")
                    .append("border-radius:15px; margin:10px;\">")
                    .append("<h3>").append(card.title()).append("</h3>")
                    .append("<p style=\"max-height:200px;\">").append(card.description()).append("</p>")
                    .append("<a class=\"challengeLink\" href=\"").append(card.route()).append("\">read more...</a>")
                    .append("</div>");
        }
        return sb.toString();
    }

}
