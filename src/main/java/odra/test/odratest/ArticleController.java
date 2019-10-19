package odra.test.odratest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ArticleController {

    @Autowired
    PikioCrawler pikioCrawler;

    @GetMapping(value = "/")
    public Article index() throws IOException {
        return pikioCrawler.crawl();

    }


}
