package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/// This class handles the index.html
public class IndexHandler extends BaseHandler{
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/index.html");
        sendResponse(exchange,200, TEXT_HTML, html);
    }
}
