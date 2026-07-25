package challenge.unionbasedsqli;

import com.sun.net.httpserver.HttpExchange;
import config.Configuration;
import exceptions.UnionSqliHandlerException;
import http.BaseHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * HTTP handler for the UNION-based SQL Injection challenge.
 * <p>
 * Serves the challenge page and processes product search requests.
 * User supplied search terms are forwarded to the
 * {@link UnionSqliService}, whose intentionally vulnerable SQL
 * implementation allows UNION-based SQL Injection.
 * </p>
 */
public class UnionSqliHandler extends BaseHandler {
    private final UnionSqliService service;
    private final Configuration challengeConfig;

    public UnionSqliHandler(UnionSqliService service, Configuration challengeConfig) {
        validate(service,challengeConfig);
        this.service = service;
        this.challengeConfig = challengeConfig;
    }

    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        String html = new String(
                readResource("/static/challenges/unionbasedsqli/unionsqli.html"),
                StandardCharsets.UTF_8);
        html = html.replace(challengeConfig.getString("challenge.htmlPlaceholder"),"" );
        sendResponse(exchange,200,TEXT_HTML,html);
    }

    @Override
    protected void doPost(HttpExchange exchange) throws IOException {
        Map<String, String> form = parseUrlEncodedData(readBody(exchange));

        String search = form.getOrDefault("search", "");

        List<Product> products = service.search(search);

        String results = renderProducts(products);

        String html = new String(readResource("/static/challenges/unionbasedsqli/unionsqli.html"));

        html = html.replace(challengeConfig.getString("challenge.htmlPlaceholder"), results);

        sendResponse(exchange,200, TEXT_HTML, html);
    }

    private String renderProducts(List<Product> products){

        if(products.isEmpty()){
            return "<p>No products found.</p>";
        }

        StringBuilder html = new StringBuilder();

        for(Product product : products){

            html.append("""
            <div class="product">
                <h3>%s</h3>
                <p>Category: %s</p>
                <p>Price: %d €</p>
                <p>%s</p>
            </div>
            """.formatted(
                    product.name(),
                    product.category(),
                    product.price(),
                    product.description()
            ));
        }

        return html.toString();
    }

    private void validate(UnionSqliService service, Configuration challengeConfig){
        if (service ==null){
            throw new UnionSqliHandlerException(
                    getClass().getName() +
                            ": UnionSqliService cannot be null,"
            );
        }
        if (challengeConfig ==null){
            throw new UnionSqliHandlerException(
                    getClass().getName() +
                            ": Challenge configuration config cannot be null."
            );
        }
    }
}
