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
    private Date crawlDate;
    private Date creationDate;
    private Integer relevance;
}
