package hexlet.code.util;

import hexlet.code.model.UrlCheck;

import java.util.List;

public class FindChecks {
    public static UrlCheck find(List<UrlCheck> list, Long id) {
        return list.stream()
                .filter(check -> id != null && id.equals(check.getUrlId()))
                .findFirst()
                .orElse(null);
    }
}
