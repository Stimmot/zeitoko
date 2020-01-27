package de.uni.koeln.odrajavascraper.scraper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.uni.koeln.odrajavascraper.entities.Article;

@Service
public class ZeitScraper extends Scraper {

	@Override
	public List<String> getNewsUrlList() {
		try {
			URL feedUrl = new URL("http://newsfeed.zeit.de/all");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));

			List<SyndEntry> entries = feed.getEntries();
			List<String> links = new ArrayList<>();
			for (SyndEntry entry : entries) {
				links.add(entry.getLink());
			}
			return links;

		} catch (FeedException fe) {
			fe.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Article scrape(String url) throws IOException, FeedException {

		// System.out.println(url);

		Document doc = openURL(url);

		// Bei Paywall, keinen Artikel speichern
		String zplus = doc.getElementsByClass("zplus-badge__text").text().toString();
		if (!zplus.isEmpty()) {
			// System.out.println("Paywall entdeckt.");
			return null;
		}

		// Rufe Vollansicht auf, falls vorhanden
		String temp = doc.body().getElementsByClass("article-pager__all").select("a").attr("href");
		if (!temp.isEmpty()) {
			doc = openURL(temp);
		}

		// HEADLINE
		String headline = doc.body().getElementsByClass("article-heading__title").text().toString();
		// System.out.println(headline);

		// TEXTBODY
		String textBody = doc.body().getElementsByClass("paragraph article__item").text().toString();
		// System.out.println(textBody);

		// AUTHOR
		String author = doc.body().getElementsByClass("byline").text().toString();
		if (author.contains(",")) {
			author = author.replaceAll(author.subSequence(author.indexOf(","), author.length()).toString(), "");
		}
		if (author.toLowerCase().contains("von")) {
			List<String> authorList = new ArrayList<String>(Arrays.asList(author.split(" ")));
			List<String> toRemove = new ArrayList<String>();
			for (String e : authorList) {
				if (e.toLowerCase().equals("von") || e.toLowerCase().equals("reportage")
						|| e.toLowerCase().equals("interview") || e.toLowerCase().contains("ein")) {
					toRemove.add(e);
				}
			}
			authorList.removeAll(toRemove);
			author = "";
			for (String e : authorList)
				author = author + e + " ";
		}

		// TOPIC
		String topic = doc.getElementsByClass("breadcrumbs__link").text().toString();
		topic = topic.replace("Start", "").replace(" ", "").replace("ZEIT", "");

		// Date
		String creationDate = doc.getElementsByClass("metadata__date").text().toString();

		Article article = new Article();

		article.setLink(url);
		article.setHeadline(headline);
		article.setSource("https://www.zeit.de");
		article.setSourceName("zeit");
		article.setTextBody(textBody);
		article.setCrawlDate(new Date());
		article.setCreationDate(creationDate);
		article.setAuthor(author);
		article.setLink(url);
		article.setTopic(topic);

		JSONObject json = new JSONObject();
		try {
			json.put("link", url);
			json.put("headline", headline);
			json.put("textBody", textBody);
			json.put("source", article.getSource());
			json.put("sourceName", article.getSourceName());
			json.put("topic", topic);
			json.put("crawlDate", article.getCrawlDate());
			json.put("creationDate", creationDate);

			//createFile(json);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return article;
	}

// previous testing method
//	public void createFile(JSONObject json) throws IOException, JSONException {
//		
//		System.out.println("Aktuelles JSON: " + json.toString());
//		
//		StringBuilder sb = new StringBuilder();
//
//		File file = new File("zeitJson");
//		if (file.createNewFile())
//			System.out.println("Datei erstellt.");
//
//		BufferedReader reader = new BufferedReader(new FileReader(file));
//
//		String st;
//		while ((st = reader.readLine()) != null)
//			sb.append(st);
//
//		reader.close();
//		System.out.println("Dateiinhalt: " + sb.toString());
//		
//		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//	    System.out.println("Continue?");
//	    String yes = myObj.nextLine();
//
//		if (sb.toString().contains(json.getString("link"))) {
//			System.out.println("Json bereits vorhanden:" + json.getString("link").toString());
//		} else {
//			System.out.println("Aktueller Stringbuilder: " + sb.toString());
//			sb.append(json.toString());
//			sb.append(System.lineSeparator());
//			System.out.println("Neuer Stringbuilder: " + sb.toString());
//			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//			sb.toString().replaceAll("\\}\\{", "\\}\n\\{");
//			writer.write(sb.toString());
//			writer.close();
//		}
//		System.out.println("===");
//		
//		
//	}

}