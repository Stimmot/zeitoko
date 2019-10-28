package odra.test.odratest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    PikioCrawler pikioCrawler;

    @GetMapping(value = "/")
    public List<Article> index() throws IOException {

        List<Article> articleList = new ArrayList<>();

        List<String> linkList = pikioCrawler.newsLinkList();

        for (String link : linkList) {
            articleList.add(pikioCrawler.crawl(link));

        }


        return articleList;

    }


}
