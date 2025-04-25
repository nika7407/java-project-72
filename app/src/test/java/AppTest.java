import hexlet.code.controller.UrlController;
import hexlet.code.database.BaseRepository;
import hexlet.code.database.DataBaseInitializer;
import hexlet.code.database.DatabaseConfig;
import hexlet.code.App;
import hexlet.code.database.UrlCheckRepository;
import hexlet.code.database.UrlRepository;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import hexlet.code.util.NamedRoutes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static hexlet.code.database.BaseRepository.dataSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class AppTest {

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
        UrlRepository.clear();
    }

    @AfterEach
    void clear() throws SQLException {
        UrlRepository.clear();
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
        UrlRepository.save(new Url("https://mvnrepository.com"));
        Context ctx = mock(Context.class);
        when(ctx.formParam("url")).thenReturn("https://mvnrepository.com");

        UrlController.postMainPage(ctx);
        verify(ctx).sessionAttribute("flash", "Страница уже существует");
    }

    @Test
    void testCheckUrlSuccess() throws SQLException {


        InputStream inputStream = DataBaseInitializer.class
                .getResourceAsStream("/testHtml.html");
        String html = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        mockWebServer.enqueue(new MockResponse().setBody(html).setResponseCode(200));

        Url url = new Url(mockServerUrl);
        UrlRepository.save(url);
        Long id = url.getId();

        Context ctx = mock(Context.class);
        when(ctx.pathParam("id")).thenReturn(id.toString());

        UrlController.postCheckUrl(ctx);

        verify(ctx).redirect(NamedRoutes.urlPath(id));
        List<UrlCheck> checks = UrlCheckRepository.getCheckEntities(id);
        assertThat(checks.get(0).getStatusCode()).isEqualTo(200);
    }

    @Test
    void testCheckUrlServerError() throws SQLException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        Url url = new Url(mockServerUrl);
        UrlRepository.save(url);
        Long id = url.getId();

        Context ctx = mock(Context.class);
        when(ctx.pathParam("id")).thenReturn(id.toString());

        UrlController.postCheckUrl(ctx);

        List<UrlCheck> checks = UrlCheckRepository.getCheckEntities(id);
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
    public void testBuildSessionPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testSaveUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = new FormBody.Builder()
                    .add("url", "https://example.com")
                    .build();

            // Отправляем POST и проверяем редирект
            var postResponse = client.post("/urls", requestBody);
            assertThat(postResponse.body()).toString().contains("Страница успешно добавлена"); // Found (Redirect)
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
