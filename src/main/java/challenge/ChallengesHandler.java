package challenge;

import com.sun.net.httpserver.HttpExchange;
import http.BaseHandler;

import java.io.IOException;

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
public class ChallengesHandler extends BaseHandler{

    /**
     * Serves the web application index page.
     *
     * @param exchange the HTTP exchange for this request
     * @throws IOException if the HTML resource cannot be read
     *                     or the response cannot be written
     */
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/challenges/challenges.html");
        sendResponse(exchange,200, TEXT_HTML, html);
    }
}
