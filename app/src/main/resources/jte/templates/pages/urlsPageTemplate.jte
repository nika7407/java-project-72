@import hexlet.code.database.UrlCheckRepository
@import hexlet.code.model.Url
@import hexlet.code.render.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.mainPageTemplate(content = @`

    <body class="container mt-5">
    <h1 class="h3 mb-3 font-weight-normal">Submited URL listL</h1>

        @for(Url url : page.getList())
            <ul class="list-group">
        <li class="list-group-item">
            <a href="${NamedRoutes.urlPath(url.getId())}" class="">${url.getName()}</a>
        </li>
            @if(UrlCheckRepository.getLastCheckStatusAndTime(url.getId()).getCreatedAt() != null)
            <li class="list-group-item">
               Status : ${UrlCheckRepository.getLastCheckStatusAndTime(url.getId()).getStatusCode()}
            </li>
                <li class="list-group-item">
                Date : ${UrlCheckRepository.getLastCheckStatusAndTime(url.getId()).getCreatedAt().toString()}
            </li>
            @endif

    </ul>
            <div class="mt-1 pt-1"> </div>
        @endfor


    <a href="${NamedRoutes.rootPath()}" class="mt-6 mb-3 pt-5 text-body-secondary fw-light fs-6">Main Page</a>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq"
            crossorigin="anonymous"></script>
    </body>


`)
