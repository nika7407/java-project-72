@import page.render.MainPage
@param MainPage page


@template.mainPageTemplate(content = @`

    @if(page.getFlash() != null)
        <div class="alert alert-primary">
            ${page.getFlash()}
        </div>
    @endif

    <form action="/urls" method="post">
        <h1 class="h3 mb-3 font-weight-normal">Input Url</h1>
        <label for="inputEmail" class="sr-only">Url</label>
        <input type="text" name="url" class="form-control" placeholder="https://some-domain.org:8080"
               value="@if(page.getUrl() != null)
            ${page.getUrl()}
        @endif" required="" autofocus="">
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    </form>
`)