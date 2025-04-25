package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class UrlCheck {
    private Long id;
    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private Timestamp createdAt;

    public UrlCheck(Integer statusCode, String title, String h1, String description, Long urlId) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public UrlCheck() {
    }
    public UrlCheck(Integer statusCode, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.createdAt = createdAt;
    }
}
