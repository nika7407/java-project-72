import database.BaseRepository;
import database.DataBaseInitializer;
import database.DatabaseConfig;
import hexlet.code.App;
import hexlet.code.model.Url;
import io.javalin.Javalin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.javalin.testtools.JavalinTest;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


import static database.BaseRepository.dataSource;

public class AppTest {

    private Javalin app;

    @BeforeEach
    void setup() throws SQLException {

        app = App.getApp();
        dataSource = DatabaseConfig.getDataSource();
        DataBaseInitializer.initialize(dataSource);
        BaseRepository.setDataSource(dataSource);
        BaseRepository.clear();
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


    @Test
    public void testBuildSessionPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testSaveUrl() {
        JavalinTest.test(app, (server, client) -> {
            Url url = new Url("https://example.com");
            url.setCreatedAt(LocalDateTime.now());
            BaseRepository.save(url);
            List<Url> urls = BaseRepository.getEntities();
            long savedId = urls.get(0).getId();
            var response = client.get("/urls/" + savedId);
            assertThat(response.body().string()).contains("https://example.com");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testGETurls() {
        JavalinTest.test(app, (server, client) -> {
            Url url = new Url("https://example.com");
            url.setCreatedAt(LocalDateTime.now());
            BaseRepository.save(url);
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }


}
