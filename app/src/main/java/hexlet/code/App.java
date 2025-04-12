package hexlet.code;

import database.BaseRepository;
import database.DataBaseInitializer;
import database.DatabaseConfig;

import io.javalin.Javalin;

import java.sql.SQLException;


public class App {

    public static Javalin getApp() throws SQLException {
        var source = DatabaseConfig.getDataSource();
        DataBaseInitializer.initialize(source);
        BaseRepository.setDataSource(source);



        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("hello world!"));


        return app;
    }

    public static void main(String[] args) throws SQLException {
        getApp().start(7070);
    }


}
