package hexlet.code;

import controller.UrlController;
import database.BaseRepository;
import database.DataBaseInitializer;
import database.DatabaseConfig;
import io.javalin.Javalin;
import java.sql.SQLException;
import static render.TemplateConfig.createTemplateEngine;
import io.javalin.rendering.template.JavalinJte;
import util.NamedRoutes;

public class App {

    public static Javalin getApp() throws SQLException {
       // creating data source h2 /OR/ postgre
        var source = DatabaseConfig.getDataSource();
        // create structure
        DataBaseInitializer.initialize(source);
        // adding data source into repo, in local environment its h2 in render its postgre
        BaseRepository.setDataSource(source);
        // clearing db
        BaseRepository.clear();

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), UrlController::getMainPage);
        app.post(NamedRoutes.urlsPath(), UrlController::postMainPage);
        app.get(NamedRoutes.urlsPath(), UrlController::getUrls);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::getUrl);
        return app;

    }

    public static void main(String[] args) throws SQLException {
        getApp().start(7070);
    }


}
