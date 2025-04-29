package hexlet.code.database;

import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlCheckRepository extends BaseRepository {

    public static void saveUrlCheck(UrlCheck check) throws SQLException {
        String sql = "INSERT INTO url_checks (status_code, title, "
                + "h1, description, url_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (var conn = dataSource.getConnection()) {

            var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, check.getStatusCode());
            stmt.setString(2, check.getTitle());
            stmt.setString(3, check.getH1());
            stmt.setString(4, check.getDescription());
            stmt.setLong(5, check.getUrlId());
            Timestamp time = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(6, time);

            stmt.executeUpdate();
            check.setCreatedAt(time);
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    check.setId(keys.getLong(1));
                }
            }

        }
    }

    public static List<UrlCheck> getCheckEntities(Long id) throws SQLException {
        String sql = "SELECT id, status_code, title, h1, "
                + "description, url_id, created_at FROM url_checks WHERE url_id = ?"
                + " ORDER BY id DESC";
        List<UrlCheck> checks = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (var resultSet = stmt.executeQuery()) {
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
        }
        return checks;
    }


    public static Map<Long, UrlCheck> getLastCheckStatusAndTime() {
        try {
            String sql = "SELECT DISTINCT ON (url_id) status_code, created_at, url_id"
                    + " FROM url_checks"
                    + " ORDER BY url_id, created_at DESC;";
            Integer status = null;
            Timestamp createdAt = null;
            Long urlId = null;
            Map<Long, UrlCheck> map = new HashMap<>();
            try (Connection conn = dataSource.getConnection();
                 var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    urlId = rs.getLong("url_id");
                    status = rs.getInt("status_code");
                    createdAt = rs.getTimestamp("created_at");

                    UrlCheck check = new UrlCheck(status, createdAt);
                    map.put(urlId, check);
                }
            }
            return map;

        } catch (SQLException e) {
            return null;
        }
    }

    public static List<UrlCheck> getCheckEntities() throws SQLException {
        String sql = "SELECT id, status_code, title, h1, description, url_id, "
                + "created_at FROM url_checks ORDER BY id DESC";
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
