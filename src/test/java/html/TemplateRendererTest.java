package html;

import config.Configuration;
import exceptions.TemplateRendererException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class TemplateRendererTest {
    // helper — builds a Configuration from varargs pairs
    private Configuration config(String... keyValues) {
        Properties props = new Properties();
        for (int i = 0; i < keyValues.length; i += 2) {
            props.setProperty(keyValues[i], keyValues[i + 1]);
        }
        return new Configuration(props);
    }

    // helper — converts string to bytes
    private byte[] bytes(String html) {
        return html.getBytes(StandardCharsets.UTF_8);
    }

    @Nested
    @DisplayName("Placeholder replacement")
    class PlaceholderReplacement {

        @Test
        @DisplayName("replaces a single placeholder with its config value")
        void replaceSinglePlaceholder() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("challenge.name", "Login SQLi"));

            String result = renderer.render(
                    bytes("<h1>{{challenge.name}}</h1>"));

            assertEquals("<h1>Login SQLi</h1>", result);
        }

        @Test
        @DisplayName("replaces multiple placeholders in one pass")
        void replaceMultiplePlaceholders() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("a", "Hello", "b", "World"));

            String result = renderer.render(bytes("{{a}} {{b}}"));

            assertEquals("Hello World", result);
        }

        @Test
        @DisplayName("leaves unmatched placeholders as-is")
        void unmatchedPlaceholderIsUntouched() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("challenge.name", "Test"));

            String result = renderer.render(
                    bytes("{{challenge.name}} {{unknown}}"));

            assertEquals("Test {{unknown}}", result);
        }

        @Test
        @DisplayName("injects content fragment into {{challenge.content}}")
        void contentFragmentInjected() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("challenge.name", "Test"));

            String result = renderer.render(
                    bytes("<div>{{challenge.content}}</div>"),
                    bytes("<form></form>")
            );

            assertTrue(result.contains("<form></form>"));
            assertFalse(result.contains("{{challenge.content}}"));
        }
    }

    @Nested
    @DisplayName("Inline code formatting")
    class InlineCodeFormatting {

        @Test
        @DisplayName("wraps backtick text in code tags")
        void backtickBecomesCodeTag() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("text", "Try `SELECT * FROM users`"));

            String result = renderer.render(bytes("{{text}}"));

            assertEquals("Try <code>SELECT * FROM users</code>", result);
        }

        @Test
        @DisplayName("encodes < and > inside backtick spans")
        void anglesBracketEncodedInsideBackticks() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("text", "`<script>alert(1)</script>`"));

            String result = renderer.render(bytes("{{text}}"));

            assertEquals(
                    "<code>&lt;script&gt;alert(1)&lt;/script&gt;</code>",
                    result);
        }

        @Test
        @DisplayName("encodes & before < and > to prevent double encoding")
        void ampersandEncodedFirst() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("text", "`&lt;div&gt;`"));

            String result = renderer.render(bytes("{{text}}"));

            assertTrue(result.contains("&amp;lt;"));
        }

        @Test
        @DisplayName("encodes quotes inside backtick spans")
        void quotesEncodedInsideBackticks() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("text", "`attr=\"value\"`"));

            String result = renderer.render(bytes("{{text}}"));

            assertTrue(result.contains("&quot;"));
        }

        @Test
        @DisplayName("does not encode HTML outside backtick spans")
        void htmlOutsideBackticksPassesThrough() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("text", "<br>not encoded `encoded`"));

            String result = renderer.render(bytes("{{text}}"));

            assertTrue(result.contains("<br>not encoded"));
            assertTrue(result.contains("<code>encoded</code>"));
        }

        @Test
        @DisplayName("handles multiple backtick spans in one value")
        void multipleBacktickSpans() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("text", "`a` and `b`"));

            String result = renderer.render(bytes("{{text}}"));

            assertEquals("<code>a</code> and <code>b</code>", result);
        }
    }

    @Nested
    @DisplayName("Guard clauses")
    class GuardClauses {

        @Test
        @DisplayName("throws when html is null")
        void throwsOnNullHtml() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("key", "value"));

            assertThrows(TemplateRendererException.class,
                    () -> renderer.render((byte[]) null));
        }

        @Test
        @DisplayName("replaces the same placeholder multiple times")
        void replacesSamePlaceholderMultipleTimes(){

            TemplateRenderer renderer =
                    new TemplateRenderer(
                            config("name","VulnaRebel"));

            String result = renderer.render(bytes("{{name}} - {{name}}"));

            assertEquals("VulnaRebel - VulnaRebel",result);
        }

        @Test
        @DisplayName("throws TemplateRendererException instead of NullPointerException")
        void neverLeaksNullPointerException() {
            TemplateRenderer renderer = new TemplateRenderer(config("key","value"));

            assertThrows(
                    TemplateRendererException.class,
                    () -> renderer.render(null));
        }

        @Test
        @DisplayName("keeps unfinished backtick blocks unchanged")
        void unfinishedBacktickIsNotConverted(){

            TemplateRenderer renderer = new TemplateRenderer(config("text","Try `SELECT"));

            String result = renderer.render(bytes("{{text}}"));

            assertEquals("Try `SELECT", result);
        }

        @Test
        @DisplayName("throws when html is empty")
        void throwsOnEmptyHtml() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("key", "value"));

            assertThrows(TemplateRendererException.class,
                    () -> renderer.render(new byte[0]));
        }

        @Test
        @DisplayName("throws when config is null")
        void throwsOnNullConfig() {
            assertThrows(TemplateRendererException.class,
                    () -> new TemplateRenderer(null));
        }

        @Test
        @DisplayName("throws when template is null in two-argument render")
        void throwsOnNullTemplate() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("key", "value"));

            assertThrows(TemplateRendererException.class,
                    () -> renderer.render(null, bytes("<form/>")));
        }

        @Test
        @DisplayName("throws when content is null in two-argument render")
        void throwsOnNullContent() {
            TemplateRenderer renderer = new TemplateRenderer(
                    config("key", "value"));

            assertThrows(TemplateRendererException.class,
                    () -> renderer.render(bytes("<div/>"), null));
        }
    }
}