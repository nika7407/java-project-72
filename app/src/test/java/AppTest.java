import database.BaseRepository;
import database.DataBaseInitializer;
import database.DatabaseConfig;
import hexlet.code.model.Url;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static database.BaseRepository.dataSource;

public class AppTest {

    @BeforeEach
    void setup() throws SQLException {
        dataSource = DatabaseConfig.getDataSource();
        DataBaseInitializer.initialize(dataSource);
        BaseRepository.setDataSource(dataSource);
    }

    @Test
    public void testAlwaysTrue() {
        Assertions.assertTrue(true);
    }

    @Test
    void testSaveAndRetrieveUrl() throws SQLException {
        Url url = new Url("https://example.com");
        url.setCreatedAt(LocalDateTime.now());

        BaseRepository.save(url);
        List<Url> urls = BaseRepository.getEntities();

        Assertions.assertEquals(1, urls.size());
        Assertions.assertEquals("https://example.com", urls.get(0).getName());
    }
}
