package hexlet.code.util;

import hexlet.code.model.UrlCheck;

import java.util.List;
import java.util.Optional;

public class FindChecks {
    public static Optional<UrlCheck> find(List<UrlCheck> list, Long id) {
        return list.stream()
                .filter(check -> check.getUrlId().equals(id))
                .findFirst();
    }
}
