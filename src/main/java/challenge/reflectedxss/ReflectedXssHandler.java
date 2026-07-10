package challenge.reflectedxss;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ReflectedXssHandlerException;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ReflectedXssHandler extends BaseHandler {

    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        if ( exchange == null ) throw new ReflectedXssHandlerException(
                getClass().getName() + ": HttpExchange cannot be null."
        );
        String rawQuery = exchange.getRequestURI().getQuery();
        if (rawQuery == null) rawQuery = "query=";
        byte[] htmlBytes = readResource("/static/challenges/reflectedxss/reflectedxss.html");
        String html = new String(htmlBytes, StandardCharsets.UTF_8);
        Map<String, String> decoded = parseUrlEncodedData(rawQuery);
        String userInput = decoded.getOrDefault("query", ""); // guard a NPE
        // INTENTIONAL VULNERABILITY:
        // User input is reflected without output encoding.
        // This simulates a reflected XSS vulnerability.
        String newHtml = html.replace("{{query}}", "You searched for: " + userInput);
        sendResponse(exchange, 200, TEXT_HTML, newHtml);
    }
}
