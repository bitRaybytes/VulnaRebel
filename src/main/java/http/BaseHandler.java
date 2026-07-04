package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


/// The `BaseHandler` class is the abstraction of any handler</br>
/// Every challenge is supposed to have its own {challengeName}Handler extending the `BaseHandler`

public abstract class BaseHandler implements HttpHandler {

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                doGet(exchange);
            } else if ("POST".equals(exchange.getRequestMethod())) {
                doPost(exchange);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    /// Default implementations
    protected void doGet(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, "Method Not Allowed");
    }

    protected void doPost(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, "Method Not Allowed");
    }

    protected void sendResponse(HttpExchange exchange,
                                int statusCode,
                                String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
