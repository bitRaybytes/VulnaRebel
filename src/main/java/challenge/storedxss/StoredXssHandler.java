package challenge.storedxss;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import exceptions.StoredXssHandlerException;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * HTTP handler for the Stored XSS challenge.
 * <p>
 * Serves the guestbook page, stores submitted messages,
 * and intentionally renders user-controlled input without
 * sanitization in order to demonstrate a Stored XSS
 * vulnerability.
 * </p>
 *
 * <p>
 * A Base64-encoded flag is stored inside a browser cookie on each
 * page request. The cookie is intentionally accessible from
 * JavaScript to support the educational objective of the challenge.
 * </p>
 */
public class StoredXssHandler extends BaseHandler {
    private final StoredXssService service;
    private final Configuration challengeConfig;

    public StoredXssHandler(StoredXssService service, Configuration challengeConfig) {
        if (service == null){
            throw new StoredXssHandlerException(
                    getClass().getName() +
                            ": Service cannot be null."
            );
        }
        this.service = service;
        this.challengeConfig = challengeConfig;
    }


    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        // encode flag as base64 - the player must use JavaScript to decode
        String encoded = Base64.getEncoder().encodeToString(
                challengeConfig.getString("challenge.flag")
                        .getBytes(StandardCharsets.UTF_8));

        // set cookie is HttpOnly intentionally omitted so JavaScript can read it
        exchange.getResponseHeaders().add("Set-Cookie",
                "flag=" + encoded + "; Path=/storedxss");

        String html = new String(
                readResource("/static/challenges/storedxss/storedxss.html"),
                StandardCharsets.UTF_8);

        List<String[]> messages = service.getComments();

        StringBuilder entries = new StringBuilder();
        for (String[] entry : messages) {
            // unsanitized — INTENTIONAL VULNERABILITY
            entries.append("<div class='entry'>")
                    .append("<strong>").append(entry[0]).append("</strong>")
                    .append("<p>").append(entry[1]).append("</p>")
                    .append("</div>");
            // INTENTIONAL VULNERABILITY: message injected without sanitization
//            entries.append(entry[0])
//                    .append(" ").append(entry[1]);
        }

        System.out.println("Entries: " + entries);
        System.out.println("List: "+ messages);

        String rendered = html.replace("{{message}}", entries.toString());
        sendResponse(exchange, 200, TEXT_HTML, rendered);
    }

    @Override
    protected void doPost(HttpExchange exchange) throws IOException {
        Map<String, String> form = parseUrlEncodedData(readBody(exchange));
        service.saveMessage(form.get("author"), form.get("message"));

        // redirect after post to "reload" the html file.
        exchange.getResponseHeaders().set("Location", "/storedxss");
        exchange.sendResponseHeaders(302, -1);
        exchange.getResponseBody().close();
    }
}
