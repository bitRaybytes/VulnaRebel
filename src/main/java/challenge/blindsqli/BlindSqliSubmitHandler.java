package challenge.blindsqli;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import http.BaseHandler;

import java.io.IOException;
import java.util.Map;

/**
 * HTTP handler for the flag submission endpoint of the
 * Blind SQL Injection challenge ({@code /blindsqli/submit}).
 * <p>
 * Accepts POST requests containing a submitted flag value and
 * validates it against the expected flag in {@code challenge.properties}.
 * No database access is required - validation is a simple string comparison.
 * </p>
 */
public class BlindSqliSubmitHandler extends BaseHandler {
    private final Configuration challengeConfig;

    /**
     * @param challengeConfig the challenge-specific configuration
     *                        containing the expected flag value
     */
    public BlindSqliSubmitHandler(Configuration challengeConfig) {
        this.challengeConfig = challengeConfig;
    }

    @Override
    protected void doPost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Map<String, String> form = parseUrlEncodedData(body);
        String submitted = form.get("flag");

        String expected = challengeConfig.getString("challenge.flag");

        if (expected.equals(submitted)) {
            sendResponse(exchange, 200, TEXT_HTML,
                    "<h2>Correct! Flag accepted.</h2><a href='/blindsqli'>Try again</a>");
        } else {
            sendResponse(exchange, 200, TEXT_HTML,
                    "<h2>Wrong flag. Keep extracting.</h2><a href='/blindsqli'>Go back</a>");
        }
    }
}
