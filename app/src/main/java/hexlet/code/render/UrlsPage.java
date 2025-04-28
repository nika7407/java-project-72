package hexlet.code.render;


import hexlet.code.database.UrlCheckRepository;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UrlsPage {
    private List<Url> list;
    private List<UrlCheck> checks;

}
