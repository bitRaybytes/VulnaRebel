package article;

/**
 * Represents a single entry on the resource index page.
 * <p>
 * A list of {@code ArticleCard} instances is passed to
 * {@link ResourceIndexHandler}, which renders each one as
 * an HTML card replacing the {@code {{cards}}} placeholder
 * in {@code resources.html}.
 * </p>
 *
 * @param title       the article title - typically the vulnerability name
 * @param description a short summary shown on the index card
 * @param route       the URL path to the full article,
 *                    e.g. {@code /resources/sql-injection}
 */
public record ArticleCard(String title, String description, String route) {
}
