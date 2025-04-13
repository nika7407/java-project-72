package controller;

import static io.javalin.rendering.template.TemplateUtil.model;

import database.BaseRepository;
import hexlet.code.model.Url;
import page.render.MainPage;
import io.javalin.http.Context;
import util.BuildUrl;
import util.NamedRoutes;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

public class UrlController {

    public static void getMainPage(Context ctx){

        MainPage page = new MainPage(null, ctx.consumeSessionAttribute("flash"));
        ctx.render("pages/mainPage.jte", model("page",page));
    }

    public static void postMainPage(Context ctx){

     try {
        var uri = URI.create(ctx.formParam("url"));
        URL url = uri.toURL();
         String name = BuildUrl.build(url);

         if(!BaseRepository.exists(name)){
           BaseRepository.save(new Url(name));
           ctx.sessionAttribute("flash", "Страница успешно добавлена");
           ctx.redirect(NamedRoutes.rootPath());
        } else {
             ctx.sessionAttribute("flash", "???????? ??? ??????????");
             ctx.redirect(NamedRoutes.rootPath());
         }


     } catch (MalformedURLException | SQLException | IllegalArgumentException e) {
       ctx.sessionAttribute("flash","???????????? URL");
         ctx.redirect(NamedRoutes.rootPath());
     }

    }

}
