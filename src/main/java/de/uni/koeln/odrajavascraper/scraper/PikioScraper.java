package de.uni.koeln.odrajavascraper.scraper;


import de.uni.koeln.odrajavascraper.entities.Article;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PikioScraper extends Scraper{




    /**
     * Scrapes all URL's of the current news articles on the piko.pl landing page
     * @return
     */
    @Override
    public List<String> getNewsUrlList() {

        try {
            Document doc = openURL("https://pikio.pl");
            List<String> links = new ArrayList<>();
            for (Element e : doc.body().getElementsByClass("news-item")) {
                links.add(e.getElementsByTag("a").attr("href"));
            }
            return links;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    /**
     * Extracts information of a pikio.pl news article into a @{@link Article} object
     *
     * @param url The URL of the news article
     * @return An Article Object with the information from the HTML page according to the URL
     * @throws IOException
     */
    public Article scrape(String url) throws IOException {

        Document doc = openURL(url);

        String headline = doc.body().getElementsByClass("page-heading").get(1).text();
        String textBody = doc.body().getElementsByClass("article-container").get(0).text();
        String author = doc.body().getElementsByClass("article-author").get(0).getElementsByTag("a").text();
        String topic = doc.body().getElementsByClass("breadcrumbs").get(0).getElementsByTag("a").get(1).text();

        Article article = new Article();
        article.setHeadline(headline);
        article.setSource("https://pikio.pl");
        article.setSourceName("pikio");
        article.setTextBody(textBody);
        article.setCrawlDate(new Date());
        article.setCreationDate(null);
        article.setAuthor(author);
        article.setLink(url);
        article.setTopic(topic);

        return article;
    }

}
