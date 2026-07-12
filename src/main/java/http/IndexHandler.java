package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * This clas serves the index html file.
 */
public class IndexHandler extends BaseHandler{
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/index.html");
        sendResponse(exchange,200, TEXT_HTML, html);
    }
}
