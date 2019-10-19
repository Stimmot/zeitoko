package odra.test.odratest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OdraTestApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        String urlStr = "https://public-history-weekly.degruyter.com/contents/";
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        int status = con.getResponseCode();

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

        List<String> topics = new ArrayList<>();

        for (Element e : doc.body().getElementsByClass("links")) {
            for (Element topicLinkElement : e.getElementsByAttribute("href")) {
                String topicLink = topicLinkElement.attr("href");
//                System.out.println(topicLink);
                topics.add(topicLink);
            }
        }

        //open every topic link and extract articles
        List<String> postList = new ArrayList<>();
        for (String topic: topics) {
            postList.addAll(openTopics(topic));
//            break;
        }

        Collections.shuffle(postList, new Random());

        Set<String> postSet = new HashSet<>();
        postSet.addAll(postList);

        int counter = 1;
        for (String postString: postSet) {
            String scheduleTime = LocalDate.now().plusDays(counter).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            System.out.println( scheduleTime + "\t" + postString);
            counter++;
        }

    }



    public List<String> openTopics(String topicURL) throws IOException {

        URL url = new URL(topicURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        int status = con.getResponseCode();

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

        Elements posts = doc.body().getElementsByClass("post");

        String postText;

        List<String> outputList = new ArrayList<>();

        for (Element post: posts) {
            String author = post.getElementsByClass("author").get(0).text();
            String time = post.getElementsByTag("time").get(0).text();
            String postLink = post.getElementsByClass("posttitle").get(0).getElementsByTag("a").attr("href");
            String postTitle = post.getElementsByClass("posttitle").get(0).getElementsByTag("a").text();
            String previewText = post.getElementsByClass("entry").get(0).getElementsByTag("p").text();
            String image = post.getElementsByClass("wp-post-image").attr("src").replaceAll("-120x120","");

//            postText = "By " + author + " on " + time +"\\n" + previewText + "\\n" + postLink;
            System.out.println(postTitle);
//            String output = "11:00" + "\t" + "From the archive:\\n\"" + postTitle.replaceAll("“","’").replaceAll("”","’") + "\"\\nBy " + author + " on " + time +"\\n\\n" + previewText + "\\n" + postLink + "\t" + image;
            String output = "11:00" + "\t" + "From the archive:\\n\"" + postTitle.replaceAll("“","’").replaceAll("”","’") + "\"\\nBy " + author + " on " + time +"\\n\\n" + postLink + "\t" + image;
            outputList.add(output);
        }

        return outputList;

    }

}
