package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DummyHandler extends BaseHandler{

    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        super.doGet(exchange);
    }
}
