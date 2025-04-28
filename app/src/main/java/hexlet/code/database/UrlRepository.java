package hexlet.code.database;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url product) throws SQLException {
        String sql = "INSERT INTO urls (name) VALUES (?)";

        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, product.getName());
                stmt.executeUpdate();

                try (var keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        product.setId(keys.getLong(1));
                    } else {
                        throw new SQLException("Failed to get generated ID");
                    }
                }
            }

            try (var stmt2 = conn.prepareStatement(
                    "SELECT created_at FROM urls WHERE id = ?")) {
                stmt2.setLong(1, product.getId());

                try (var resultSet = stmt2.executeQuery()) {
                    if (resultSet.next()) {
                        product.setCreatedAt(Timestamp.valueOf(resultSet.getTimestamp("created_at").toLocalDateTime()));
                    }
                }
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        String sql = "SELECT id, name, created_at FROM urls ORDER BY created_at DESC";
        List<Url> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
             var resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                Url url = new Url(id, name, createdAt);
                list.add(url);
            }
        }
        return list;
    }

    public static boolean exists(String name) throws SQLException {
        String query = "SELECT * FROM urls WHERE name = (?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public static Optional<Url> getUrlByName(String name) throws SQLException {
        String query = "SELECT id, name, created_at FROM urls WHERE name = (?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String nameToSet = rs.getString("name");
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    Url url = new Url(id, name, createdAt);

                    return Optional.of(url);
                }
                return Optional.empty();
            }
        }
    }

    public static Optional<Url> getUrlById(Long id) throws SQLException {
        String query = "SELECT id, name, created_at FROM urls WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long resultId = rs.getLong("id");
                    String name = rs.getString("name");
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    Url url = new Url(resultId, name, createdAt);

                    return Optional.of(url);
                }
                return Optional.empty();
            }
        }
    }

    public static void clear() throws SQLException {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM urls");
            stmt.execute("DELETE FROM url_checks");

        }
    }


}
