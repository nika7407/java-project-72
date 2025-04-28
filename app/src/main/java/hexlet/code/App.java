package hexlet.code;

import hexlet.code.controller.UrlController;
import hexlet.code.database.BaseRepository;
import hexlet.code.database.DataBaseInitializer;
import hexlet.code.database.DatabaseConfig;
import io.javalin.Javalin;
import java.sql.SQLException;
import static hexlet.code.render.TemplateConfig.createTemplateEngine;
import io.javalin.rendering.template.JavalinJte;
import hexlet.code.util.NamedRoutes;

public class App {

    public static Javalin getApp() throws SQLException {
       // creating data source h2 OR postgre
        var source = DatabaseConfig.getDataSource();
        // create tables if they do not exist
        DataBaseInitializer.initialize(source);
        // adding dataSource into repo, in local environment its h2 in render its postgre
        BaseRepository.setDataSource(source);


        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), UrlController::getMainPage);
        app.post(NamedRoutes.urlsPath(), UrlController::postMainPage);
        app.get(NamedRoutes.urlsPath(), UrlController::getUrls);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::getUrl);
        app.post(NamedRoutes.urlChecksPath("{id}"), UrlController::postCheckUrl);
        return app;

    }

    public static void main(String[] args) throws SQLException {
        getApp().start(7070);
    }


}
