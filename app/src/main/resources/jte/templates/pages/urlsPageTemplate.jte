@import hexlet.code.model.Url
@import page.render.UrlsPage
@import util.NamedRoutes
@param UrlsPage page

@template.mainPageTemplate(content = @`

    <body class="container mt-5">
    <h1 class="h3 mb-3 font-weight-normal">Submited URL listL</h1>
    <div class="list-group">
        @for(Url url : page.getList())
        <a href="${NamedRoutes.urlPath(url.getId())}" class="list-group-item list-group-item-action">${url.getName()}</a>
        @endfor
    </div >
    <a href="${NamedRoutes.rootPath()}" class="mt-6 mb-3 pt-5 text-body-secondary fw-light fs-6">Main Page</a>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq"
            crossorigin="anonymous"></script>
    </body>


`)
