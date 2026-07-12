package challenge.loginsqli;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import exceptions.LoginSqliHandlerException;
import exceptions.LoginSqliServiceException;
import http.BaseHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Handler for {@link LoginSqliChallenge} to handle requests and responses.<br>
 * Extends {@link BaseHandler}. Does need a {@link LoginSqliService} to manage the authorization and a {@link Configuration}
 * to load the challenges-specific {@code configuration.properties}.
 */
public class LoginSqliHandler extends BaseHandler {
    private final LoginSqliService service;
    private final Configuration challengeConfig;

    public LoginSqliHandler(LoginSqliService service, Configuration challengeConfig) {
        validate(service, challengeConfig);
        this.service = service;
        this.challengeConfig = challengeConfig;
    }

    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/challenges/login/login.html");
        sendResponse(exchange, 200, TEXT_HTML, html);
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
