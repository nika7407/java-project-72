package hexlet.code.render;


import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class UrlsPage {
    private List<Url> list;
    private Map<Long, UrlCheck> checks;

}
