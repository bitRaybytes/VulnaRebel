package challenge.loginsqli;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import exceptions.LoginSqliHandlerException;
import exceptions.LoginSqliServiceException;
import html.TemplateRenderer;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Handler for {@link LoginSqliChallenge} to handle requests and responses.
 * Extends {@link BaseHandler}. Does need a {@link LoginSqliService} to manage the authorization and a {@link Configuration}
 * to load the challenges-specific {@code challenge.properties}.
 */
public class LoginSqliHandler extends BaseHandler {
    private final LoginSqliService service;
    private final Configuration challengeConfig;
    private final TemplateRenderer renderer;

    public LoginSqliHandler(LoginSqliService service, Configuration challengeConfig) {
        validate(service, challengeConfig);
        this.service = service;
        this.challengeConfig = challengeConfig;
        this.renderer = new TemplateRenderer(challengeConfig);

    }

    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] template = readResource("/static/challenges/challenge-template.html");
        byte[] content = readResource("/static/challenges/login/content.html");
        String rendered = renderer.render(template, content)
                .replace("{{queryResult}}", "");
        sendResponse(exchange, 200, TEXT_HTML, rendered);
    }

    @Override
    protected void doPost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Map<String, String> pairs = parseUrlEncodedData(body);

        try {
            boolean attempt = service.attemptLogin(
                    pairs.get("username"),pairs.get("password"));

            if (attempt){
                sendResponse(exchange,200,TEXT_HTML,
                        challengeConfig.getString("challenge.flag"));
            }else {
                sendResponse(exchange,200,TEXT_HTML,
                        challengeConfig.getString("challenge.message.invalid"));
            }

        } catch (InterruptedException e) {
            sendResponse(exchange,500, TEXT_PLAIN,"Something went wrong.");
        } catch (LoginSqliServiceException e) {
            throw new LoginSqliHandlerException(
                    LoginSqliHandler.class.getName()+
                            ": Failure occurred during database connection. ", e
            );
        }
    }

    private void validate(LoginSqliService service, Configuration challengeConfig){
        if (service ==null){
            throw new LoginSqliHandlerException(
                    getClass().getName() +
                            ": LoginService cannot be null,"
            );
        }
        if (challengeConfig ==null){
            throw new LoginSqliHandlerException(
                    getClass().getName() +
                            ": Challenge's config cannot be null."
            );
        }
    }
}
