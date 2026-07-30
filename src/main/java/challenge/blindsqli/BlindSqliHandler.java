package challenge.blindsqli;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import exceptions.BlindSqliServiceException;
import html.TemplateRenderer;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP handler for the Blind SQL Injection challenge.
 * <p>
 * Serves the challenge page on GET and processes search queries on POST.
 * The POST handler intentionally passes unsanitized user input directly
 * into a SQL query — no {@link java.sql.PreparedStatement} is used.
 * </p>
 * <p>
 * The response reveals only whether a user was found or not,
 * forcing the player to extract data character by character
 * using {@code SUBSTRING}-based payloads.
 * </p>
 */
public class BlindSqliHandler extends BaseHandler {
    private final BlindSqliService service;
    private final Configuration challengeConfig;
    private final TemplateRenderer renderer;

    /**
     * @param service To check user input
     * @param challengeConfig for challenge-specific configurations
     */
    public BlindSqliHandler(BlindSqliService service, Configuration challengeConfig) {
        this.service = service;
        this.challengeConfig = challengeConfig;
        this.renderer = new TemplateRenderer(challengeConfig);
    }

    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] template = readResource("/static/challenges/challenge-template.html");
        byte[] content  = readResource("/static/challenges/blindsqli/content.html");
        String rendered = renderer.render(template,content)
                .replace("{{resultDisplay}}","");
        sendResponse(exchange,200,TEXT_HTML,rendered);
    }

    @Override
    protected void doPost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Map<String, String> search = parseUrlEncodedData(body);
        try{
            boolean found = service.searchAttempt(search.get("username"));

            String queryResult = found
                    ? "<span style='color:green;'>User found.</span>"
                    : "<span style='color:red;'>User not found.</span>";

            byte[] template = readResource("/static/challenges/challenge-template.html");
            byte[] content  = readResource("/static/challenges/blindsqli/content.html");
            String rendererd = renderer.render(template,content)
                    .replace("{{resultDisplay}}", queryResult);

            sendResponse(exchange, 200, TEXT_HTML, rendererd);
        }  catch (BlindSqliServiceException e) {
            sendResponse(exchange, 500, TEXT_PLAIN, "Something went wrong.");
         }
    }
}

