package odra.test.odratest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    PikioScraper pikioScraper;

    @GetMapping(value = "/")
    public List<Article> index() throws IOException {

        List<Article> articleList = new ArrayList<>();

        for (String link : pikioScraper.getNewsUrlList()) {
            articleList.add(pikioScraper.scrape(link));
        }

        return articleList;

    }


}
