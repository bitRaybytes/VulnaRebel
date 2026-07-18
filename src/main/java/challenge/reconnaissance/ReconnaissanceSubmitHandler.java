package challenge.reconnaissance;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Handles flag submission for the Reconnaissance challenge at {@code /flag}.
 * <p>
 * Validates the submitted access code against {@code challenge.flag}
 * in {@code challenge.properties}. On success, redirects to
 * {@code challenge.redirect}. On failure, re-serves the welcome page
 * with an "Access denied" message injected into {@code {{flagResult}}}.
 * </p>
 */
public class ReconnaissanceSubmitHandler extends BaseHandler {
    private final Configuration challengeConfig;

    public ReconnaissanceSubmitHandler(Configuration challengeConfig) {
        this.challengeConfig = challengeConfig;
    }

    @Override
    protected void doPost(HttpExchange exchange) throws IOException {
        Map<String, String> form = parseUrlEncodedData(readBody(exchange));
        String submitted = form.get("flag").trim();
        System.out.println("submitted flag: " + submitted);
        String expected  = challengeConfig.getString("challenge.flag");
        System.out.println("expected flag: " + expected);

        if (expected.equals(submitted)) {
            exchange.getResponseHeaders().set("Location",
                    challengeConfig.getString("challenge.redirect"));
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();
        } else {
            byte[] html = readResource("/static/challenges/reconnaissance/reconnaissance.html");
            String rendered = new String(html, StandardCharsets.UTF_8)
                    .replace("{{flagResult}}",
                            "<span style='color:red;'>Access denied. Keep looking.</span>");
            sendResponse(exchange, 200, TEXT_HTML, rendered);
        }
    }
}
