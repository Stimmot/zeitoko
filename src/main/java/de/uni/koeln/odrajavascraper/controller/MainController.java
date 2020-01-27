package de.uni.koeln.odrajavascraper.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rometools.rome.io.FeedException;

import de.uni.koeln.odrajavascraper.entities.Article;
import de.uni.koeln.odrajavascraper.scraper.ZeitScraper;
import de.uni.koeln.odrajavascraper.scraper.OkoScraper;
import de.uni.koeln.odrajavascraper.scraper.PikioScraper;
import de.uni.koeln.odrajavascraper.scraper.SpiegelScraper;

/**
 * Kontrolliert welche Daten bei welchem Pfad aufruf ausgegeben werden
 */
@RestController
public class MainController {

    @Autowired
    PikioScraper pikioScraper;

    @Autowired
    SpiegelScraper spiegelScraper;
    
    @Autowired
    ZeitScraper zeitScraper;
    
    @Autowired
    OkoScraper okoScraper;

    @GetMapping(value = "/pikio")
    public List<Article> index() throws IOException {

        List<Article> articleList = new ArrayList<>();

        for (String link : pikioScraper.getNewsUrlList()) {
            articleList.add(pikioScraper.scrape(link));
        }

        return articleList;

    }


    @GetMapping(value = "/spiegel")
    public List<Article> spiegel() throws IOException {

        List<Article> articleList = new ArrayList<>();

        for (String link : spiegelScraper.getNewsUrlList()) {
            articleList.add(spiegelScraper.scrape(link));
        }

        return articleList;

    }
    
    @GetMapping(value = "/zeit")
    public List<Article> zeit() throws IOException, FeedException {

    	List<Article> articleList = new ArrayList<>();

        for (String link : zeitScraper.getNewsUrlList()) {
            articleList.add(zeitScraper.scrape(link));
        }
        
//        for (Article article: articleList)
//        	article.toString();
        
        return articleList;

    }
    
    @GetMapping(value = "/oko")
    public List<Article> oko() throws IOException, FeedException {

    	List<Article> articleList = new ArrayList<>();

        for (String link : okoScraper.getNewsUrlList()) {
            articleList.add(okoScraper.scrape(link));
        }

        return articleList;

    }
    
    


    @GetMapping(value = "/")
    public String empty() throws IOException {


        return "<h1>No route found. Please go to /spiegel, /pikio, /oko or /zeit</h1>";

    }


}
