package hexlet.code.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
//id, name, createdAt.

@Setter
@Getter
@AllArgsConstructor
public class Url {

    private Long id;   // <- this.
    private  String name;
    private Timestamp createdAt;


    // for adding last checks int the url index page
    private Integer status;
    private Timestamp lastCheck;


    public Url(String name) {
        this.name = name;
    }

    public Url(long id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    /**
     * Sets creation timestamp from LocalDateTime.
     * When overriding, ensure proper conversion to Timestamp
     * @param createdAt creation time in LocalDateTime format
     */
    public final void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = Timestamp.valueOf(createdAt);
    }
}
