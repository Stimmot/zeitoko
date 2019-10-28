package odra.test.odratest;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PikioScraper {


    /**
     * Opens the URL and returns a parsed JSoup Document
     *
     * @param urlStr the url to open
     * @return A parsed JSoup Document with the content of the HTML page
     * @throws IOException
     */
    private Document openURL(String urlStr) throws IOException {
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
        return Jsoup.parse(siteContent);

    }

    /**
     * Scrapes all URL's of the current news articles on the piko.pl landing page
     * @return
     */
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
