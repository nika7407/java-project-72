package gg.jte.generated.ondemand.pages;
import hexlet.code.model.UrlCheck;
import page.render.UrlPage;
import util.NamedRoutes;
@SuppressWarnings("unchecked")
public final class JteurlPageGenerated {
	public static final String JTE_NAME = "pages/urlPage.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,3,5,5,5,5,9,9,9,10,10,10,11,11,11,14,14,14,14,14,14,14,14,14,15,15,15,15,15,15,15,15,15,17,17,17,17,17,17,17,17,17,43,43,46,46,46,49,49,49,52,52,52,55,55,55,58,58,58,61,61,61,64,64,72,72,72,72,72,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.JtemainPageTemplateGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <body class=\"container mt-5\">\r\n    <h1 class=\"h3 mb-3 font-weight-normal\">Submited URL</h1>\r\n    <ul class=\"list-group\">\r\n        <li class=\"list-group-item active\">");
				jteOutput.setContext("li", null);
				jteOutput.writeUserContent(page.getUrl().getName());
				jteOutput.writeContent("</li>\r\n        <li class=\"list-group-item\">Id: ");
				jteOutput.setContext("li", null);
				jteOutput.writeUserContent(page.getUrl().getId());
				jteOutput.writeContent("</li>\r\n        <li class=\"list-group-item\">Created at: ");
				jteOutput.setContext("li", null);
				jteOutput.writeUserContent(page.getUrl().getCreatedAt().toString());
				jteOutput.writeContent("</li>\r\n    </ul>\r\n\r\n    <a");
				var __jte_html_attribute_0 = NamedRoutes.urlsPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" href=\"");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("a", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" class=\"mt-5 me-3 mb-3 text-body-secondary fw-light fs-6\">Url List</a>\r\n    <a");
				var __jte_html_attribute_1 = NamedRoutes.rootPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" href=\"");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("a", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" class=\"mt-5 mb-3 text-body-secondary fw-light fs-6\">Main Page</a>\r\n\r\n    <form");
				var __jte_html_attribute_2 = NamedRoutes.urlChecksPath(page.getUrl().getId());
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" method=\"post\">\r\n        <button class=\"btn btn-lg btn-primary btn-block mt-5 mb-5\" type=\"submit\">Check</button>\r\n    </form>\r\n\r\n    <h1 class=\"h3 mb-3 font-weight-normal\">URL Check</h1>\r\n    <div class=\"container text-center\">\r\n        <div class=\"row\">\r\n            <div class=\"col\">\r\n                ID\r\n            </div>\r\n            <div class=\"col\">\r\n                Return Code\r\n            </div>\r\n            <div class=\"col\">\r\n                title\r\n            </div>\r\n            <div class=\"col\">\r\n                h1\r\n            </div>\r\n            <div class=\"col\">\r\n                description\r\n            </div>\r\n            <div class=\"col\">\r\n                Date\r\n            </div>\r\n        </div>\r\n        ");
				for (UrlCheck check : page.getCheck()) {
					jteOutput.writeContent("\r\n            <div class=\"row\">\r\n                <div class=\"col\">\r\n                    ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(check.getId());
					jteOutput.writeContent("\r\n                </div>\r\n                <div class=\"col\">\r\n                    ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(check.getStatusCode());
					jteOutput.writeContent("\r\n                </div>\r\n                <div class=\"col\">\r\n                    ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(check.getTitle());
					jteOutput.writeContent("\r\n                </div>\r\n                <div class=\"col\">\r\n                    ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(check.getH1());
					jteOutput.writeContent("\r\n                </div>\r\n                <div class=\"col\">\r\n                    ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(check.getDescription());
					jteOutput.writeContent("\r\n                </div>\r\n                <div class=\"col\">\r\n                    ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(check.getCreatedAt().toString());
					jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n    </div>\r\n\r\n\r\n    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js\"\r\n            integrity=\"sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq\"\r\n            crossorigin=\"anonymous\"></script>\r\n    </body>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlPage page = (UrlPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
