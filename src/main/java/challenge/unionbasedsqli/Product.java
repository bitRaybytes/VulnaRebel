package challenge.unionbasedsqli;

/**
 * Represents a product returned by the Union SQL Injection challenge.
 *
 * @param name product name
 * @param category product category
 * @param price product price
 * @param description product description
 */
public record Product(
        String name,
        String category,
        int price,
        String description
) {
}
