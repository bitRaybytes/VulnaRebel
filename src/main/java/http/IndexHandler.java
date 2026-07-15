package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Serves the web application index page listing all available
 * challenges. Navigation provides a link to the
 * <p>
 * Handles {@code GET /index} and renders the static
 * {@code index.html} dashboard - a card holding all linking to
 * individual challenges.
 * </p>
 * <p>
 * Has no dependencies beyond {@link http.BaseHandler} - the index
 * page is static and requires no configuration or rendering.
 * </p>
 */
public class IndexHandler extends BaseHandler{

    /**
     * Serves the web application index page.
     *
     * @param exchange the HTTP exchange for this request
     * @throws IOException if the HTML resource cannot be read
     *                     or the response cannot be written
     */
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/index.html");
        sendResponse(exchange,200, TEXT_HTML, html);
    }
}
