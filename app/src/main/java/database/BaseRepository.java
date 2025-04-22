package database;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.Setter;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseRepository {

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

            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");

            stmt.execute("TRUNCATE TABLE url_checks RESTART IDENTITY");
            stmt.execute("TRUNCATE TABLE urls RESTART IDENTITY");

            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }

    public static void saveUrlCheck(UrlCheck check) throws SQLException {
        String sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, check.getStatusCode());
            stmt.setString(2, check.getTitle());
            stmt.setString(3, check.getH1());
            stmt.setString(4,check.getDescription());
            stmt.setLong(5,check.getUrlId());
            stmt.setTimestamp(6,check.getCreatedAt());
            stmt.executeUpdate();
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    check.setId(keys.getLong(1));
                }
            }
        }
    }

    public static List<UrlCheck> getCheckEntities(Long id) throws SQLException {
        String sql = "SELECT id, status_code, title, h1, description, url_id, created_at FROM url_checks WHERE url_id = " + id + " ORDER BY id DESC";
        List<UrlCheck> checks = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
             var resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                UrlCheck check = new UrlCheck();
                check.setId(resultSet.getLong("id"));
                check.setStatusCode(resultSet.getInt("status_code"));
                check.setTitle(resultSet.getString("title"));
                check.setH1(resultSet.getString("h1"));
                check.setDescription(resultSet.getString("description"));
                check.setUrlId(resultSet.getLong("url_id"));
                check.setCreatedAt(Timestamp.from(resultSet.getTimestamp("created_at").toInstant()));

                checks.add(check);
            }
        }
        return checks;
    }

    public static List<UrlCheck> getCheckEntities() throws SQLException {
        String sql = "SELECT id, status_code, title, h1, description, url_id, created_at FROM url_checks ORDER BY id DESC";
        List<UrlCheck> checks = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
             var resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                UrlCheck check = new UrlCheck();
                check.setId(resultSet.getLong("id"));
                check.setStatusCode(resultSet.getInt("status_code"));
                check.setTitle(resultSet.getString("title"));
                check.setH1(resultSet.getString("h1"));
                check.setDescription(resultSet.getString("description"));
                check.setUrlId(resultSet.getLong("url_id"));
                check.setCreatedAt(Timestamp.from(resultSet.getTimestamp("created_at").toInstant()));

                checks.add(check);
            }
        }
        return checks;
    }

}

