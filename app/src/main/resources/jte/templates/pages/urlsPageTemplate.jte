@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck
@import hexlet.code.render.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page

@template.mainPageTemplate(content = @`
    <body class="container mt-5">
    <h1 class="h3 mb-3 font-weight-normal">Submitted URL List</h1>

    @for(Url url : page.getList())
        <ul class="list-group">
            <li class="list-group-item">
                <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
            </li>

            @if(page.getChecks() != null && page.getChecks().get(url.getId()) != null && page.getChecks().get(url.getId()).getStatusCode() != null)
                <li class="list-group-item">
                    Status: ${page.getChecks().get(url.getId()).getStatusCode()}
                </li>
                <li class="list-group-item">
                    Date: ${page.getChecks().get(url.getId()).getCreatedAt().toString()}
                </li>
            @endif
        </ul>
        <div class="mt-1 pt-1"></div>
    @endfor

    <a href="${NamedRoutes.rootPath()}" class="mt-6 mb-3 pt-5 text-body-secondary fw-light fs-6">Main Page</a>
    </body>
`)
