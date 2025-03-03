package me.catzy.invester.objects.article;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import me.catzy.invester.Utils;
import me.catzy.invester.generic.GenericServiceImpl;
import me.catzy.invester.objects.marketEvent.MarketEventService;
import me.catzy.invester.objects.webdriver.WebDriverService;

@Service
public class ArticleService extends GenericServiceImpl<Article, Long> {
	
	public ArticleService(ArticleRepository repository) {
		super(repository);
	}
	
	@Autowired ArticleRepository repo;
	@Autowired MarketEventService serviceAI;
	@Autowired WebDriverService serviceWeb;
	
	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
	
	@Scheduled(fixedRate = 10, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
	public void checkForAnyNews() throws MalformedURLException, Exception {
		logger.info("checking for news...");
		
		List<String> rssFeeds = new ArrayList<String>();
		rssFeeds.add("https://www.fxstreet.com/rss/news");
		rssFeeds.add("https://www.fxstreet.com/rss/stocks");
		rssFeeds.add("https://pl.investing.com/rss/news_14.rss"); //gospodarcze
		rssFeeds.add("https://pl.investing.com/rss/news_95.rss"); //o wskanikach ekonomicznych
		rssFeeds.add("https://pl.investing.com/rss/stock_Indices.rss");
		rssFeeds.add("https://pl.investing.com/rss/commodities_Metals.rss");
		rssFeeds.add("https://pl.investing.com/rss/market_overview_Fundamental.rss"); //analiza fundamentalna

		for(String s : rssFeeds) {
			try {
				processRSS(new URL(s));
			} catch (Exception e) {
				logger.error("failed processing one of privided RSS's");
				e.printStackTrace();
			}
		}
		serviceWeb.close();
	}
	
	public void processRSS(URL url) throws Exception {
		NodeList items = loadDoc(url);
        
        List<Article> articles = new ArrayList<Article>();

        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            
            Article article = new Article();
            article.title = getItem(item,"title");
            article.url = getItem(item,"link");
            article.content = getItem(item,"description");
            
            String date = item.getElementsByTagName("pubDate").item(0).getTextContent();
	        article.setTimestamp(new Timestamp(parseDate(date).getTime()));

	        articles.add(article);
        }
        
        for(Article a : articles) {
        	Article f = repo.findByUrl(a.url);
        	if(f != null) {
        		continue;
        	}
        	logger.info("new article: "+ a.title);
        	
        	a.content = scrapeArticleContent(a);
        	
        	a = repo.save(a);
        	
        	Thread.sleep(1000);
        }
	}
	
	private String scrapeArticleContent(Article a) {
		try {
			WebDriver driver = serviceWeb.get();

			driver.get(a.url);

			// 1
			List<WebElement> el = driver.findElements(By.id("article"));

			// 2
			if (el.size() == 0) {
				logger.warn("article web-element not found - trying alternative method, URL:" + a.getUrl());
				el = driver.findElements(By.className("articlePage"));
			}

			// 3
			if (el.size() == 0) {
				logger.warn("article web-element not found AGAIN - trying alternative method (FX), URL:" + a.getUrl());
				el = driver.findElements(By.className("fxs_article_content"));
			}
			
			return Utils.extractArticleText(el.get(0));
    	} catch (Exception e) {
    		logger.error("failed scraping article: " + a.url);
    		e.printStackTrace();
    	}
		return null;
	}
	
	private NodeList loadDoc(URL url) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder builder = factory.newDocumentBuilder();
		
        InputStream inputStream = url.openStream();
        Document document = builder.parse(inputStream);

        //normalising DOC
        document.getDocumentElement().normalize();
        NodeList items = document.getElementsByTagName("item");
        
        return items;
	}
	
	private String getItem(Element element, String item) {
		NodeList nl = element.getElementsByTagName(item);
		if(nl.getLength() == 0) {
			return null;
		}
		return nl.item(0).getTextContent();
	}
	
	
	
	
	//multiple data formats support
	private DateFormat[] formatters = new DateFormat[]{
		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss X", Locale.ENGLISH),
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH),
		new SimpleDateFormat("MMM dd, yyyy HH:mm z", Locale.ENGLISH)
	};
	private Date parseDate(String s) {
		for(DateFormat df : formatters) {
			try {return df.parse(s);} catch (ParseException e) {}
		}
		logger.error("failed parsing date: " + s);
		return null;
	}
}
