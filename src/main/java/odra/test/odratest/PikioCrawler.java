package odra.test.odratest;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

@Service
public class PikioCrawler {



    public Article crawl() throws IOException {


        String urlStr = "https://pikio.pl/8-rocznica-smolenska-niemcy-loty/";
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        String siteContent = content.toString();
        Document doc = Jsoup.parse(siteContent);

        String headline = doc.body().getElementsByClass("page-heading").get(1).text();
        String textBody = doc.body().getElementsByClass("article-container").get(0).text();

        Article article = new Article();
        article.setHeadline(headline);
        article.setSource(urlStr);
        article.setTextBody(textBody);
        article.setCrawlDate(new Date());
        article.setCreationDate(new Date());
        article.setRelevance(5);
        return article;
    }

}
