package me.catzy.invester.objects.service.article;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.annotation.PostConstruct;
import me.catzy.invester.Utils;
import me.catzy.invester.generic.GenericServiceImpl;
import me.catzy.invester.tasks.ai.AIService;
import me.catzy.invester.tasks.ai.AIService.Estimation;
import me.catzy.invester.tasks.ai.WebDriverService;

@Service
public class ArticleService extends GenericServiceImpl<Article, Long> {
	
	public ArticleService(ArticleRepository repository) {
		super(repository);
	}
	
	@Autowired ArticleRepository repo;
	@Autowired AIService serviceAI;
	@Autowired WebDriverService serviceWeb;
	
	@Override
	public List<Article> findAll() {
		return repo.findAll();
	}
	
	@PostConstruct
	public void init() {
		try {
			startLoader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Thread articleLoader = null;
	public void startLoader() {
		articleLoader = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					loadArticles();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		articleLoader.start();
	}
	
	public void loadArticles() throws MalformedURLException, Exception {
		List<String> rssFeeds = new ArrayList<String>();
		rssFeeds.add("https://www.fxstreet.com/rss/news");
		rssFeeds.add("https://pl.investing.com/rss/news_14.rss"); //gospodarcze
		rssFeeds.add("https://pl.investing.com/rss/news_95.rss"); //o wskanikach ekonomicznych
		rssFeeds.add("https://pl.investing.com/rss/stock_Indices.rss");
		rssFeeds.add("https://pl.investing.com/rss/commodities_Metals.rss");
		rssFeeds.add("https://pl.investing.com/rss/market_overview_Fundamental.rss"); //analiza fundamentalna
		
		for(String s : rssFeeds) {
			try {
				loadArticle(new URL(s));
			} catch (Exception e) {
				System.out.println("FAILED PROCESSING ARTICLE:");
				e.printStackTrace();
			}
		}
		serviceWeb.close();
	}
	
	private String getItem(Element element, String item) {
		NodeList nl = element.getElementsByTagName(item);
		if(nl.getLength() == 0) {
			return null;
		}
		return nl.item(0).getTextContent();
	}
	
	private static DateFormat fxFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss X", Locale.ENGLISH);
	private static DateFormat investingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	private static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm z", Locale.ENGLISH);
	private Date parseDate(String s) {
		try {return fxFormat.parse(s);} catch (ParseException e) {}
		try {return investingFormat.parse(s);} catch (ParseException e) {}
		try {return sdf.parse(s);} catch (ParseException e) {}
		System.out.println("failed parsing:");
		System.out.println(s);
		return null;
	}
	
	private NodeList loadDoc(URL url) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = url.openStream();
        Document document = builder.parse(inputStream);

        // Normalizacja dokumentu
        document.getDocumentElement().normalize();
        NodeList items = document.getElementsByTagName("item");
        
        return items;
	}
	
	public void loadArticle(URL url) throws Exception {
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
        
        for(Article article : articles) {
        	Article f = repo.findByUrl(article.url);
        	if(f != null) {
        		System.out.println("omitting");
        		continue;
        	}
        	
        	System.out.println("Przetwarzam: "+ article.title);
        	
        	try {
        		WebDriver driver = serviceWeb.get();
            	
            	if(article.content == null) {
    				driver.get(article.url);
    				WebElement el = driver.findElement(By.id("article"));
    				article.content = Utils.extractArticleText(el);
    				Thread.sleep(1000);
    			}
            	
            	article = repo.save(article);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
	}
	
	
	public void processArticles() {
		List<Article> articles = repo.findAll();
		
		for(Article a : articles) {
			
			if(a.getImpactBad() != null && a.getImpactGood() != null) {
				continue;
			}
			
			try {
				processArticle(a);
			} catch (Exception e) {
				System.out.println("failed!");
				e.printStackTrace();
			}
		}
	}
	public void processArticle(Article a) throws URISyntaxException, IOException, InterruptedException {
		
		System.out.println("Processing article:");
		System.out.println(a.url);
		
		Estimation es = serviceAI.askChatbot(a.getContent());
		
		a.setImpactGood(es.impactGood);
		a.setImpactBad(es.impactBad);
		a.setScream(es.scream);
		
		System.out.println("good:" + a.getImpactGood() + ", bad:" + a.getImpactBad());
		System.out.println(">" + a.getScream());
		
		repo.save(a);
	}
}
