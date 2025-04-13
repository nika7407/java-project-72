package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class DatabaseConfig {
    private static final String DEFAULT_JDBC_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";


    public static HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        String jdbcUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", DEFAULT_JDBC_URL);

        config.setJdbcUrl(jdbcUrl);

        if (jdbcUrl.startsWith("jdbc:h2:")) {
            config.setUsername("sa");
            config.setPassword("");
        }

        return new HikariDataSource(config);
    }
}
