package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/// This class handles the index.html by sending the input of the resource to the
public class IndexHandler extends BaseHandler{
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("static/index.html");
        sendResponse(exchange,200,TEXT_HTML, html);
    }
}
