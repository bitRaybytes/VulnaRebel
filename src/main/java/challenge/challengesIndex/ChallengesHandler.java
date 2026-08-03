package challenge.challengesIndex;

import challenge.ChallengeLink;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ChallengesHandlerException;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serves the web application challenges page listing all available
 * challenges. Navigation provides a link to the resource section.
 * <p>
 * Handles {@code GET /challenges} and renders the static
 * {@code challenges.html} dashboard - a card holding all linking to
 * individual challenges.
 * </p>
 * <p>
 * Has no dependencies beyond {@link http.BaseHandler} - the challenges
 * page is static and requires no configuration or rendering.
 * </p>
 */
public class ChallengesHandler extends BaseHandler {

    private final List<ChallengeLink> links;

    /**
     * Returns an object of a {@code ChallengeLink} that is responsible for
     * the challenge-specific linking on the {@code challenge.html} page.
     * @param links a {@link List} containing {@code ChallengeLink} objects.
     */
    public ChallengesHandler(List<ChallengeLink> links){
        validateLinks(links);
        this.links = links;
    }
    /**
     * Serves the web application challenges page where each challenge is listed.
     *
     * @param exchange the HTTP exchange for this request
     * @throws IOException if the HTML resource cannot be read
     *                     or the response cannot be written
     */
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/challenges/challenges.html");
        String allLinks = links.stream()
                .map(ChallengeLink::toHtml)
                .collect(Collectors.joining());

        String replaced = new String(html, StandardCharsets.UTF_8)
                .replace("{{challenge.link}}", allLinks);
        sendResponse(exchange,200, TEXT_HTML, replaced);
    }

    private void validateLinks (List<ChallengeLink> links){
        if ( links == null || links.isEmpty()){
            throw new ChallengesHandlerException(
                    getClass().getName() +
                            ": List of ChallengeLink's cannot be null or empty."
            );
        }
    }
}
