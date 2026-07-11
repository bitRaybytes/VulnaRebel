package challenge.loginsqli;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import exceptions.LoginHandlerException;
import exceptions.LoginServiceException;
import http.BaseHandler;

import java.io.IOException;
import java.util.Map;

/// Handles the authentication logic for the SQL Injection login challenge.
/// Deliberately uses string concatenation instead of PreparedStatement
/// to demonstrate SQL injection vulnerability.
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
        } catch (LoginServiceException e) {
            throw new LoginHandlerException(
                    LoginSqliHandler.class.getName()+
                            ": Failure occurred during database connection. ", e
            );
        }
    }

    private void validate(LoginSqliService service, Configuration challengeConfig){
        if (service ==null){
            throw new LoginHandlerException(
                    getClass().getName() +
                            ": LoginService cannot be null,"
            );
        }
        if (challengeConfig ==null){
            throw new LoginHandlerException(
                    getClass().getName() +
                            ": Challenge's config cannot be null."
            );
        }
    }
}
