package challenge.storedxss;

import database.DatabaseManager;
import exceptions.StoredXssServiceException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for the Stored XSS challenge.
 * <p>
 * Encapsulates all database interactions related to the guestbook,
 * including storing new entries and retrieving previously submitted
 * messages.
 * </p>
 *
 * <p>
 * The SQL statements are intentionally constructed using string
 * concatenation to support the educational purpose of VulnaRebel.
 * They must not be considered production-ready code.
 * </p>
 */
public class StoredXssService {
    private final DatabaseManager manager;

    public StoredXssService(DatabaseManager manager) {
        if (manager == null){
            throw new StoredXssServiceException(
                    getClass().getName() +
                            ": DatabaseManger cannot be null."
            );
        }
        this.manager = manager;
    }


    public void saveMessage(String author, String message) {
        String query = "INSERT INTO storedxss_guestbook (author, message) " +
                "VALUES ('" + author + "', '" + message + "')";
        try (var conn = manager.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException | InterruptedException e) {
            throw new StoredXssServiceException(
                    getClass().getName() + ": Failed to save message.", e);
        }
    }

    public List<String[]> getComments() {
        List<String[]> list = new ArrayList<>();
        String query = "SELECT author, message FROM storedxss_guestbook ORDER BY id ASC";
        try (var conn = manager.getConnection();
             var pstmt = conn.prepareStatement(query);
             var rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("author"),
                        rs.getString("message")
                });
            }
            return list;
        } catch (SQLException | InterruptedException e) {
            throw new StoredXssServiceException(
                    getClass().getName() + ": Failed to retrieve comments.", e);
        }
    }
}
