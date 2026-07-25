package challenge.unionbasedsqli;

import database.DatabaseManager;
import exceptions.UnionSqliServiceException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UnionSqliService {
    private final DatabaseManager manager;

    public UnionSqliService(DatabaseManager manager) {
        this.manager = manager;
    }

    /**
     *
     * @param input user input
     * @return a {@code List<Product>} containing all database items
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
