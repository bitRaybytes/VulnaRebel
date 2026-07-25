package challenge.unionbasedsqli;

import database.DatabaseManager;
import exceptions.UnionSqliServiceException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for the UNION-based SQL Injection challenge.
 * <p>
 * Executes product search queries against the application's database.
 * The SQL statement intentionally concatenates user input directly into
 * the query string in order to demonstrate a UNION-based SQL Injection
 * vulnerability.
 * </p>
 * <p>
 * This implementation is deliberately insecure and exists solely for
 * educational purposes inside the VulnaRebel training application.
 * </p>
 */
public class UnionSqliService {
    private final DatabaseManager manager;

    /**
     * Creates a new service using the supplied database manager.
     *
     * @param manager the database connection manager used to execute
     *                SQL statements
     * @throws UnionSqliServiceException if {@code manager} is {@code null}
     */
    public UnionSqliService(DatabaseManager manager) {
        this.manager = manager;
    }

    /**
     * Searches the product catalog for products whose names match the
     * supplied search string.
     * <p>
     * The query intentionally concatenates the user input directly into
     * the SQL statement in order to expose a UNION-based SQL Injection
     * vulnerability.
     * Supplying an empty string returns all available products.
     * </p>
     *
     * @param input the user supplied search string
     * @return a list containing all matching {@link Product} objects
     * @throws UnionSqliServiceException if the database query cannot
     *         be executed
     */
    public List<Product> search(String input){
        // No guard check of method parameter, because empty String should return all Products
        String query =
                "SELECT name, category, price, description " +
                "FROM products " +
                "WHERE name LIKE '%" + input + "%'";

        List<Product> products = new ArrayList<>();

        try(var conn = manager.getConnection();
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query);
        ){
            while (rs.next()){
                products.add(new Product(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("price"),
                        rs.getString("description"))
                );
            }
            return products;
        } catch (SQLException | InterruptedException e) {
            throw new UnionSqliServiceException(
                    "Failed to search products.", e);
        }
    }
}
