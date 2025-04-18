package database;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;
import lombok.Setter;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseRepository  {

    @Setter
    public static HikariDataSource dataSource;

    public static void save(Url product) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setTimestamp(2, product.getCreatedAt());
            stmt.executeUpdate();
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getLong(1));
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

    public static boolean exists(String name)  {
        try {
            return getEntities().stream()
                    .anyMatch(value -> value.getName().equals(name));
        } catch (SQLException e) {
            return false;
        }
    }

    public static Optional<Url> getUrlByName(String name) throws SQLException  {
        return getEntities().stream()
                    .filter(url -> url.getName().equals(name))
                    .findFirst();

    }

    public static Optional<Url> getUrlById(Long id) throws SQLException  {
        return getEntities().stream()
                .filter(url -> url.getId().equals(id))
                .findFirst();

    }

    public static void clear() throws SQLException {
        String sql = "TRUNCATE TABLE urls;";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();
        }
    }
}
