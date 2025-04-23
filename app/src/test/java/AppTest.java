import controller.UrlController;
import database.BaseRepository;
import database.DataBaseInitializer;
import database.DatabaseConfig;
import hexlet.code.App;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import io.javalin.Javalin;
import io.javalin.http.Context;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.javalin.testtools.JavalinTest;
import util.NamedRoutes;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


import static database.BaseRepository.dataSource;

public class AppTest {

    private static Javalin app;
    private static MockWebServer mockWebServer;
    private static String mockServerUrl;

    @AfterAll
    public static void shutdownMockServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeAll
    public static void setupMockServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockServerUrl = mockWebServer.url("/").toString();
    }

    @BeforeEach
    void setup() throws SQLException {

        app = App.getApp();
        dataSource = DatabaseConfig.getDataSource();
        DataBaseInitializer.initialize(dataSource);
        BaseRepository.setDataSource(dataSource);
        BaseRepository.clear();
    }

    @AfterEach
    void clear() throws SQLException {
        BaseRepository.clear();
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Test
    void testAddValidUrl() throws SQLException {
        Context ctx = mock(Context.class);
        when(ctx.formParam("url")).thenReturn(mockServerUrl);

        UrlController.postMainPage(ctx);

        verify(ctx).sessionAttribute("flash", "Страница успешно добавлена");
    }

    @Test
    void testAddInvalidUrl() {
        Context ctx = mock(Context.class);
        when(ctx.formParam("url")).thenReturn("invalid-url");

        UrlController.postMainPage(ctx);

        verify(ctx).sessionAttribute("flash", "Некорректный URL");
    }


    @Test
    void testAddExistingUrl() throws SQLException {
        BaseRepository.save(new Url("https://mvnrepository.com"));
        Context ctx = mock(Context.class);
        when(ctx.formParam("url")).thenReturn("https://mvnrepository.com");

        UrlController.postMainPage(ctx);
        verify(ctx).sessionAttribute("flash", "Страница уже существует");
    }

    @Test
    void testCheckUrlSuccess() throws SQLException {
        String html = """
            <!DOCTYPE html>
            <html>
                <head>
                    <title>Test Page</title>
                    <meta name="description" content="Test Description">
                </head>
                <body>
                    <h1>Test Header</h1>
                </body>
            </html>""";
        mockWebServer.enqueue(new MockResponse().setBody(html).setResponseCode(200));

        Url url = new Url(mockServerUrl);
        BaseRepository.save(url);
        Long id = url.getId();

        Context ctx = mock(Context.class);
        when(ctx.pathParam("id")).thenReturn(id.toString());

        UrlController.postCheckUrl(ctx);

        verify(ctx).redirect(NamedRoutes.urlPath(id));
        List<UrlCheck> checks = BaseRepository.getCheckEntities(id);
        assertThat(checks.get(0).getStatusCode()).isEqualTo(200);
    }

    @Test
    void testCheckUrlServerError() throws SQLException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        Url url = new Url(mockServerUrl);
        BaseRepository.save(url);
        Long id = url.getId();

        Context ctx = mock(Context.class);
        when(ctx.pathParam("id")).thenReturn(id.toString());

        UrlController.postCheckUrl(ctx);

        List<UrlCheck> checks = BaseRepository.getCheckEntities(id);
        assertThat(checks.get(0).getStatusCode()).isEqualTo(500);
    }

    @Test
    void testCheckNonExistentUrl() {
        Context ctx = mock(Context.class);
        when(ctx.pathParam("id")).thenReturn("999");

        UrlController.postCheckUrl(ctx);

        verify(ctx).status(404);
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
