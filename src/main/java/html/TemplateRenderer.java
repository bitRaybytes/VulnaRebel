package html;

import config.Configuration;
import exceptions.TemplateRendererException;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Renders HTML templates by replacing {@code {{key}}} placeholders
 * with values from a {@link Configuration} file.
 * <p>
 * Designed for serving resource articles - a single
 * {@code article-template.html} file acts as the shell,
 * and an article-specific {@code .properties} file supplies
 * the content values (title, body, difficulty, etc.).
 * </p>
 * <p>
 * Inline code formatting is applied automatically - text wrapped
 * in backticks (e.g. {@code `SELECT * FROM users`}) is converted
 * to {@code <code>} tags before injection into the template.
 * </p>
 * <p>
 * This class is {@code final} - it is not intended to be subclassed.
 * </p>
 */
public final class TemplateRenderer {

    private final Configuration config;

    /**
     * @param config the article-specific configuration whose
     *               key-value pairs fill the template placeholders
     * @throws TemplateRendererException if {@code config} is null
     */
    public TemplateRenderer(Configuration config) {
        if (config == null){
            throw new TemplateRendererException(
                    getClass().getName() +
                            ": Configuration cannot be null."
            );
        }
        this.config = config;
    }

    /**
     * Backtick-wrapped text in property values is converted to
     * {@code <code>} tags with HTML special characters encoded,
     * preventing injected content from executing as real HTML.
     *
     * <p>
     * Each key in the configuration is matched against placeholders of
     * the form {@code {{key}}} in the HTML. Unmatched placeholders
     * are left as-is. Backtick-wrapped text in property values is
     * converted to {@code <code>} tags before injection.
     * </p>
     *
     * <pre>{@code
     * byte[] template = readResource("/static/article/article-template.html");
     * String rendered = renderer.render(template);
     * sendResponse(exchange, 200, TEXT_HTML, rendered);
     * }</pre>
     *
     * @param html the raw HTML template as a byte array
     * @return the rendered HTML with placeholders replaced
     *         and inline code formatting applied
     * @throws TemplateRendererException if {@code html} is null or empty
     */
    public String render(byte[] html) throws TemplateRendererException {
        if (html == null || html.length == 0) {
            throw new TemplateRendererException(
                    getClass().getName() + ": HTML template cannot be null or empty.");
        }
        String rendered = new String(html, StandardCharsets.UTF_8);
        for (Map.Entry<String, String> entry : config.asMap().entrySet()) {
            String value = applyInlineFormatting(entry.getValue());
            rendered = rendered.replace(
                    "{{" + entry.getKey() + "}}",
                    value
            );
        }
        return rendered;
    }

    // Converts backtick-wrapped text to <code> tags.
    // HTML special characters inside backticks are encoded first to prevent
    // the browser from interpreting injected content as real HTML elements.
    // Example: `<img src=x onerror=alert(1)>`
    //       => <code>&lt;img src=x onerror=alert(1)&gt;</code>
    private String applyInlineFormatting(String text) {
        Matcher matcher = Pattern.compile("`([^`]+)`").matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String encoded = matcher.group(1)
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
            matcher.appendReplacement(result,
                    Matcher.quoteReplacement("<code>" + encoded + "</code>"));
        }
        matcher.appendTail(result);
        return result.toString();
    }

}
