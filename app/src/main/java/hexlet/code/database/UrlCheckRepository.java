package hexlet.code.database;

import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    check.setId(keys.getLong(1));
                }
            }


            try (var stmt2 = conn.prepareStatement(
                    "SELECT created_at FROM url_checks WHERE id = ?")) {
                stmt2.setLong(1, check.getId());

                try (var resultSet = stmt2.executeQuery()) {
                    if (resultSet.next()) {
                        check.setCreatedAt(Timestamp.valueOf(resultSet.getTimestamp("created_at").toLocalDateTime()));
                    }
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


    public static UrlCheck getLastCheckStatusAndTime(Long id)  {
        try {
            String sql = "SELECT status_code, created_at FROM url_checks WHERE url_id = " + id
                    + " ORDER BY id DESC LIMIT 1";
            Integer status = null;
            Timestamp time = null;
            try (Connection conn = dataSource.getConnection();
                 var stmt = conn.prepareStatement(sql);
                 var resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    status = resultSet.getInt("status_code");
                    time = Timestamp.from(resultSet.getTimestamp("created_at").toInstant());
                }
            }
            return new UrlCheck(status, time);
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
