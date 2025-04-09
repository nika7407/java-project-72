package hexlet.code;

import io.javalin.Javalin;
// import io.javalin.rendering.template.JavalinJte;
// import static io.javalin.rendering.template.TemplateUtil.model;
// import io.javalin.rendering.template.JavalinJte;

public class App {

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("hello world!"));

        return app;
    }

    public static void main(String[] args) {
        getApp().start(7070);
    }
}
