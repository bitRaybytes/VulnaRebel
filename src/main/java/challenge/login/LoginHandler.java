package challenge.login;

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
public class LoginHandler extends BaseHandler {
    private final LoginService service;
    private final Configuration challengeConfig;

    public LoginHandler(LoginService service, Configuration challengeConfig) {
        this.service = service;
        this.challengeConfig = challengeConfig;
    }

    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/login.html");
        sendResponse(exchange, 200, TEXT_HTML, html);
    }

    @Override
    protected void doPost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Map<String, String> pairs = parseFormBody(body);

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
                    LoginHandler.class.getName()+
                            ": Failure occurred during database connection. ", e
            );
        }
    }
}
