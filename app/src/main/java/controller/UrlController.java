package controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import database.BaseRepository;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import io.javalin.http.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import page.render.MainPage;
import page.render.UrlPage;
import page.render.UrlsPage;
import util.BuildUrl;
import util.NamedRoutes;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
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

            if (!BaseRepository.exists(name)) {
                BaseRepository.save(new Url(name));
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
            List<Url> urls  = BaseRepository.getEntities();
            List<UrlCheck> checks = BaseRepository.getCheckEntities();

            urls.forEach(url -> {
                checks.stream()
                        .filter(check -> check.getUrlId().equals(url.getId()))
                        .max(Comparator.comparing(UrlCheck::getCreatedAt))
                        .ifPresent(latestCheck -> {
                            url.setStatus(latestCheck.getStatusCode());
                            url.setLastCheck(latestCheck.getCreatedAt());
                        });
            });

            var page = new UrlsPage(urls);
            ctx.render("pages/urlsPageTemplate.jte", model("page", page));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getUrl(Context ctx) {
        try {
            Long id = Long.valueOf(ctx.pathParam("id"));
            var url = BaseRepository.getUrlById(id).get();

            List<UrlCheck> list  = BaseRepository.getCheckEntities(id);

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
            Optional<Url> urlOptional = BaseRepository.getUrlById(id);
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

            BaseRepository.saveUrlCheck(check);
            url.setStatus(response.getStatus());
            url.setLastCheck(check.getCreatedAt());
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
