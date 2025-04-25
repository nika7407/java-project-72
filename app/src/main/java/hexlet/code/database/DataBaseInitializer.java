package hexlet.code.database;

import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DataBaseInitializer {
    public static void initialize(HikariDataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            InputStream inputStream = DataBaseInitializer.class
                    .getResourceAsStream("/schema.sql");
            String sql = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            stmt.execute(sql);
        }
    }
}
