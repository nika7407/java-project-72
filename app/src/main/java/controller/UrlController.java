package controller;

import static io.javalin.rendering.template.TemplateUtil.model;

import database.BaseRepository;
import hexlet.code.model.Url;
import page.render.MainPage;
import io.javalin.http.Context;
import page.render.UrlPage;
import page.render.UrlsPage;
import util.BuildUrl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class UrlController {

    public static void getMainPage(Context ctx) {

        MainPage page = new MainPage(null, ctx.consumeSessionAttribute("flash"));
        ctx.render("pages/mainPage.jte", model("page", page));
    }

    public static void getMainPage(Context ctx, String url) {

        MainPage page = new MainPage(url, ctx.consumeSessionAttribute("flash"));
        ctx.render("pages/mainPage.jte", model("page", page));
    }

    public static void postMainPage(Context ctx) {
        String input = ctx.formParam("url");
        try {
            assert input != null;
            var uri = URI.create(input);
            URL url = uri.toURL();
            String name = BuildUrl.build(url);

            if (!BaseRepository.exists(name)) {
                BaseRepository.save(new Url(name));
                ctx.sessionAttribute("flash", "Page Saved");
                getMainPage(ctx);

            } else {
                ctx.sessionAttribute("flash", "Page Already exists");
                getMainPage(ctx, input);
            }


        } catch (MalformedURLException | SQLException | IllegalArgumentException e) {
            ctx.sessionAttribute("flash", "Not a URL");
            getMainPage(ctx, input);
        }

    }

    public static void getUrls(Context ctx) {
        try {
            List<Url> list = BaseRepository.getEntities();
            var page = new UrlsPage(list);
            ctx.render("pages/urlsPageTemplate.jte", model("page", page));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getUrl(Context ctx) {
        try {
            Long id = Long.valueOf(ctx.pathParam("id"));
            var url = BaseRepository.getUrlById(id).get();
            var page = new UrlPage(url);
            ctx.render("pages/urlPage.jte", model("page", page));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            ctx.sessionAttribute("flash", "Such element does not exists");
            ctx.status(404);
            getMainPage(ctx, null);
        }

    }




}
