package hexlet.code.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;

public class BaseRepository {
    @Setter
    public static HikariDataSource dataSource;
}
