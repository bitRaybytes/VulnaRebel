package challenge.reflectedxss;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ReflectedXssHandlerException;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP handler for the Reflected Xss challenge.
 * <p>
 * Serves the challenge page on GET.
 * </p>
 * <p>
 * There is no response, since this challenge will run on client-side.
 * To retrieve the flag, the user has to inject a specific malicious {@code JavaScript}
 * payload into the search form to read from a dummy environment data-set set up sitting in the script-tag of the html.
 * </p>
 */
public class ReflectedXssHandler extends BaseHandler {

    /**
     * User input is reflected without output encoding.
     * This simulates a reflected XSS vulnerability.
     * @param exchange a {@link HttpExchange} to read the data from.
     * @throws IOException if an error occurs.
     */
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        if ( exchange == null ) throw new ReflectedXssHandlerException(
                getClass().getName() + ": HttpExchange cannot be null."
        );
        byte[] htmlBytes = readResource("/static/challenges/reflectedxss/reflectedxss.html");
        String html = new String(htmlBytes, StandardCharsets.UTF_8);
        String rawQuery = exchange.getRequestURI().getQuery();

        String userInput = "";
        if (rawQuery != null && rawQuery.contains("=")) {
            Map<String, String> decoded = parseUrlEncodedData(rawQuery);
            userInput = decoded.getOrDefault("query", "");
        }

        // INTENTIONAL VULNERABILITY:
        // User input is reflected without output encoding.
        // This simulates a reflected XSS vulnerability.
        String newHtml = html.replace("{{query}}", "You searched for: " + userInput);
        sendResponse(exchange, 200, TEXT_HTML, newHtml);
    }
}
