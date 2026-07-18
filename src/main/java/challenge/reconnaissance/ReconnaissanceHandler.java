package challenge.reconnaissance;

import com.sun.net.httpserver.HttpExchange;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Serves the VulnaRebel welcome page at {@code /}.
 * <p>
 * The page contains a hidden HTML comment with the access code,
 * simulating a developer credential left in production source code.
 * The {@code {{flagResult}}} placeholder is replaced with an empty
 * string on initial load.
 * </p>
 */
public class ReconnaissanceHandler extends BaseHandler {
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        String html = new String(
                readResource("/static/challenges/reconnaissance/reconnaissance.html"), StandardCharsets.UTF_8)
                .replace("{{flagResult}}", "");
        sendResponse(exchange,200,TEXT_HTML,html);
    }
}
