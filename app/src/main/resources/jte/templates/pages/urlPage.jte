@import hexlet.code.model.UrlCheck
@import page.render.UrlPage
@import util.NamedRoutes
@param UrlPage page

@template.mainPageTemplate(content = @`
    <body class="container mt-5">
    <h1 class="h3 mb-3 font-weight-normal">Submited URL</h1>
    <ul class="list-group">
        <li class="list-group-item active">${page.getUrl().getName()}</li>
        <li class="list-group-item">Id: ${page.getUrl().getId()}</li>
        <li class="list-group-item">Created at: ${page.getUrl().getCreatedAt().toString()}</li>
    </ul>

    <a href="${NamedRoutes.urlsPath()}" class="mt-5 me-3 mb-3 text-body-secondary fw-light fs-6">Url List</a>
    <a href="${NamedRoutes.rootPath()}" class="mt-5 mb-3 text-body-secondary fw-light fs-6">Main Page</a>

    <form action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" method="post">
        <button class="btn btn-lg btn-primary btn-block mt-5 mb-5" type="submit">Check</button>
    </form>

    <h1 class="h3 mb-3 font-weight-normal">URL Check</h1>
    <div class="container text-center">
        <div class="row">
            <div class="col">
                ID
            </div>
            <div class="col">
                Return Code
            </div>
            <div class="col">
                title
            </div>
            <div class="col">
                h1
            </div>
            <div class="col">
                description
            </div>
            <div class="col">
                Date
            </div>
        </div>
        @for(UrlCheck check : page.getCheck())
            <div class="row">
                <div class="col">
                    ${check.getId()}
                </div>
                <div class="col">
                    ${check.getStatusCode()}
                </div>
                <div class="col">
                    ${check.getTitle()}
                </div>
                <div class="col">
                    ${check.getH1()}
                </div>
                <div class="col">
                    ${check.getDescription()}
                </div>
                <div class="col">
                    ${check.getCreatedAt().toString()}
                </div>
            </div>
        @endfor
    </div>


    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq"
            crossorigin="anonymous"></script>
    </body>
`)