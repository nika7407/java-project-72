package hexlet.code.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
//id, name, createdAt.

@Setter
@Getter
@AllArgsConstructor
public class Url {

    private Long id;   // <- this.
    private String name;
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }

    public Url(long id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}
