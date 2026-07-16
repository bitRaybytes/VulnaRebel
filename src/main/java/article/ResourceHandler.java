package article;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ResourceHandlerException;
import html.TemplateRenderer;
import http.BaseHandler;

import java.io.IOException;

/**
 * Serves a single resource article by rendering an HTML template
 * with article-specific content supplied by a {@link TemplateRenderer}.
 * <p>
 * Each instance is bound to one article at construction time -
 * the {@link TemplateRenderer} encapsulates the article's
 * {@link config.Configuration} and replaces {@code {{key}}}
 * placeholders in {@code article-template.html} accordingly.
 * </p>
 *
 * <p>
 * Register one {@code ResourceHandler} per article route in
 * {@link core.Main}:
 * </p>
 *
 * <pre>{@code
 * router.register(new Route("/resources/sql-injection",
 *     new ResourceHandler(new TemplateRenderer(
 *         ConfigurationLoader.load("resources/sql-injection.properties")))));
 * }</pre>
 */
public class ResourceHandler extends BaseHandler {

    private final TemplateRenderer renderer;

    /**
     * @param renderer the pre-configured renderer for this article -
     *                 encapsulates the article's configuration and
     *                 placeholder replacement logic
     * @throws ResourceHandlerException if {@code renderer} is null
     */
    public ResourceHandler(TemplateRenderer renderer) {
        if (renderer == null){
            throw new ResourceHandlerException(
                    getClass().getName() +
                            ": TemplateRenderer cannot be null."
            );
        }
        this.renderer = renderer;
    }

    /**
     * Renders and serves the article page for this handler's
     * configured resource.
     *
     * @param exchange the HTTP exchange for this request
     * @throws IOException if the HTML template cannot be read
     *                     or the response cannot be written
     */
    @Override
    protected void doGet(HttpExchange exchange) throws IOException {
        byte[] html = readResource("/static/article/article-template.html");
        String rendered = renderer.render(html);
        sendResponse(exchange,200,TEXT_HTML,rendered);
    }


}
