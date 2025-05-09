package hexlet.code.render;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlPage {
    private Url url;
    private List<UrlCheck> check;
    private String flash;
}
