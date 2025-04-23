package gg.jte.generated.ondemand;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JtemainPageTemplateGenerated {
	public static final String JTE_NAME = "mainPageTemplate.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,12,12,12,12,24,24,24,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content content) {
		jteOutput.writeContent("\r\n<!doctype html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n    <title>Url Check</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-SgOJa3DmI69IUzQ2PVdRZhwQ+dy64/BUtbMJw1MZ8t5HZApcHrRKUc4W0kG879m7\" crossorigin=\"anonymous\">\r\n</head>\r\n\r\n<h2>");
		jteOutput.setContext("h2", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("</h2>\r\n<body class=\"d-flex flex-column min-vh-100\">\r\n<footer class=\"mt-auto\">\r\n    <div class=\"text-center  p-3\" style=\"background-color: rgba(0, 0, 0, 0.05);\">\r\n        Nika's Link Tester\r\n    </div>\r\n</footer>\r\n</body>\r\n\r\n\r\n\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Content content = (Content)params.get("content");
		render(jteOutput, jteHtmlInterceptor, content);
	}
}
