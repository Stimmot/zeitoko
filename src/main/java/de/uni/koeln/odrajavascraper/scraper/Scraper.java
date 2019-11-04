package de.uni.koeln.odrajavascraper.scraper;

import com.rometools.rome.io.FeedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class Scraper {

    /**
     * Opens the URL and returns a parsed JSoup Document
     *
     * @param urlStr the url to open
     * @return A parsed JSoup Document with the content of the HTML page
     * @throws IOException
     */
    public Document openURL(String urlStr) throws IOException {
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



    public abstract List<String> getNewsUrlList() throws IOException, FeedException;


}
