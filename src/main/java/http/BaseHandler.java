package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.BaseHandlerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/// The `BaseHandler` class is the abstraction of any handler</br>
/// Every challenge is supposed to have its own {challengeName}Handler extending the `BaseHandler`

public abstract class BaseHandler implements HttpHandler {

    protected static final String TEXT_HTML = "text/html; charset=utf-8";
    protected static final String TEXT_PLAIN = "text/plain; charset=utf-8";

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                doGet(exchange);
            } else if ("POST".equals(exchange.getRequestMethod())) {
                doPost(exchange);
            } else {
                sendResponse(exchange, 405, TEXT_HTML,"Method Not Allowed");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, TEXT_HTML,"Internal Server Error");
        }
    }

    /// Default implementations
    protected void doGet(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, TEXT_HTML,"Method Not Allowed");
    }
    protected void doPost(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, TEXT_HTML,"Method Not Allowed");
    }

    protected void sendResponse(HttpExchange exchange,
                                int statusCode,
                                String contentType,
                                String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    /// Corresponding sendResponse method to overload that takes bytes directly without a String round-trip
    protected void sendResponse(HttpExchange exchange,
                                int statusCode,
                                String contentType,
                                byte[] body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    /// This method is to read the body of a request or response
    protected String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()){
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /// This method is to read the html file from the classpath's resource
    protected byte[] readResource(String classpathPath) throws IOException {
        try (InputStream is = BaseHandler.class.getResourceAsStream(classpathPath)) {
            if (is == null) {
                throw new BaseHandlerException(BaseHandler.class.getName() +
                        ": Resource not found: " + classpathPath);
            }
            return is.readAllBytes();
        }
    }

    protected Map<String,String> parseFormBody(String body){
        if (body == null || body.isBlank()){
            throw new BaseHandlerException(
                    BaseHandler.class.getName() +
                            "Body cannot be empty: "+body);
        }
        Map<String, String> form = new HashMap<>();


        String[] pairs = body.split("&");
        for (String pair: pairs){
            String[] parts = pair.split("=",2);

            // edge case for malformed pairs with no "="
            if (parts.length == 2){
                String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                form.put(parts[0],value);
            }else {
                throw new BaseHandlerException(BaseHandler.class.getName()
                        + ": There is no pair for: "+ Arrays.toString(parts));
            }
        }

        if (!form.isEmpty()) return form;

        throw new BaseHandlerException(BaseHandler.class.getName() + ": Map cannot be null. A body needs to be given.");
    }
}
