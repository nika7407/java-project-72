package hexlet.code.database;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url product) throws SQLException {

        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getLong(1));
                    product.setCreatedAt(keys.getTimestamp(2));
                }
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        String sql = "SELECT id, name, created_at FROM urls";
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

    public static boolean exists(String name) {
        try {
            return getEntities().stream()
                    .anyMatch(value -> value.getName().equals(name));
        } catch (SQLException e) {
            return false;
        }
    }

    public static Optional<Url> getUrlByName(String name) throws SQLException {
        return getEntities().stream()
                .filter(url -> url.getName().equals(name))
                .findFirst();

    }

    public static Optional<Url> getUrlById(Long id) throws SQLException {
        return getEntities().stream()
                .filter(url -> url.getId().equals(id))
                .findFirst();

    }

    public static void clear() throws SQLException {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {

            String database = conn.getMetaData().getDatabaseProductName();

            if ("H2".equals(database)) {
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                stmt.execute("TRUNCATE TABLE url_checks RESTART IDENTITY");
                stmt.execute("TRUNCATE TABLE urls RESTART IDENTITY");
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
            } else if ("PostgreSQL".equals(database)) {
                stmt.execute("TRUNCATE TABLE urls, url_checks RESTART IDENTITY CASCADE");
            } else {
                throw new SQLException("Unsupported database: " + database);
            }
        }
    }


}
