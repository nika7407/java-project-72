@import page.render.MainPage
@import util.NamedRoutes
@param MainPage page


@template.mainPageTemplate(content = @`




    <body class="container mt-5">
    <form action="/urls" method="post">
        <h1 class="h3 mb-3 font-weight-normal">Submit URL</h1>
        @if(page.getFlash() != null)
            <div class="alert alert-primary">
                ${page.getFlash()}
            </div>
        @endif
        <div class="form-group">
            <label for="urlInput" class="form-label">URL</label>
            <input  type="text"
                    name="url"
                    class="form-control"
                    placeholder="https://some-domain.org:8080"
                    value="${page.getUrl() != null ? page.getUrl() : ""}"
                    required="" autofocus="">
        </div>
        <button class="btn btn-lg btn-primary btn-block mt-3" type="submit">Submit</button>
    </form>
    <a href="${NamedRoutes.urlsPath()}" class="mt-5 mb-3 text-body-secondary fw-light fs-6">Url List</a>
    <p class="mt-5 mb-3 text-body-secondary fw-light fs-6">Nika's Link Tester</p>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq"
            crossorigin="anonymous"></script>
    </body>



`)