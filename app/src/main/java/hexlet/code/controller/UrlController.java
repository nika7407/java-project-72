package hexlet.code.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hexlet.code.database.UrlCheckRepository;
import hexlet.code.database.UrlRepository;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.render.MainPage;
import hexlet.code.render.UrlPage;
import hexlet.code.render.UrlsPage;
import hexlet.code.util.BuildUrl;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static io.javalin.rendering.template.TemplateUtil.model;

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

            if (!UrlRepository.exists(name)) {
                UrlRepository.save(new Url(name));
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                getMainPage(ctx);

            } else {
                ctx.sessionAttribute("flash", "Страница уже существует");
                getMainPage(ctx, input);
            }


        } catch (MalformedURLException | SQLException | IllegalArgumentException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            getMainPage(ctx, input);
        }

    }

    public static void getUrls(Context ctx) {
        try {
            List<Url> urls  = UrlRepository.getEntities();
            List<UrlCheck> checks = new ArrayList<>();
            for (Url url : urls) {
                var lastCheck = UrlCheckRepository.getLastCheckStatusAndTime(url.getId());
                if (lastCheck != null) {
                    checks.add(lastCheck);
                }

            }
            var page = new UrlsPage(urls, checks);
            ctx.render("pages/urlsPageTemplate.jte", model("page", page));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getUrl(Context ctx) {
        try {
            Long id = Long.valueOf(ctx.pathParam("id"));
            var url = UrlRepository.getUrlById(id).get();

            List<UrlCheck> list  = UrlCheckRepository.getCheckEntities(id);

            var page = new UrlPage(url, list, ctx.consumeSessionAttribute("flash"));
            ctx.render("pages/urlPage.jte", model("page", page));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            ctx.sessionAttribute("flash", "Such element does not exists");
            ctx.status(404);
            getMainPage(ctx, null);
        }

    }

    public static void postCheckUrl(Context ctx) {
        long id;

        try {
            id = Long.parseLong(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID");
            return;
        }

        try {
            Optional<Url> urlOptional = UrlRepository.getUrlById(id);
            if (urlOptional.isEmpty()) {
                ctx.status(404);
                return;
            }
            Url url = urlOptional.get();

            HttpResponse<String> response = null;
            try {
                response = Unirest.get(url.getName()).asString();
            } catch (UnirestException e) {
                ctx.status(500);
                ctx.sessionAttribute("flash", "Некорректный адрес");
                getUrl(ctx);
            }

            Document doc = Jsoup.parse(response.getBody());
            String title = doc.title();
            String h1 = doc.selectFirst("h1") != null ? doc.selectFirst("h1").text() : null;
            String description = doc.selectFirst("meta[name=description]") != null
                    ? doc.selectFirst("meta[name=description]").attr("content") : null;

            UrlCheck check = new UrlCheck(
                    response.getStatus(),
                    title,
                    h1,
                    description,
                    id
            );

            UrlCheckRepository.saveUrlCheck(check);
            ctx.redirect(NamedRoutes.urlPath(id));

        } catch (SQLException e) {
            ctx.status(500);
            ctx.sessionAttribute("flash", "Ошибка Базы данных");
            getUrl(ctx);
        } catch (Exception e) {
            ctx.status(500);
            ctx.sessionAttribute("flash", "Некорректный адрес");
            getUrl(ctx);
        }
    }



}
