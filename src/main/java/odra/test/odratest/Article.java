package odra.test.odratest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private String headline;
    private String textBody;
    private String source;
    private String sourceName;
    private String author;
    private String topic;
    private String link;
    private Date crawlDate;
    private String creationDate;
}
