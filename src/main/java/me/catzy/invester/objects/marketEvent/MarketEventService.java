package me.catzy.invester.objects.marketEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import me.catzy.invester.generic.GenericServiceImpl;
import me.catzy.invester.objects.article.Article;
import me.catzy.invester.objects.article.ArticleRepository;
import me.catzy.invester.objects.lmStudio.LMStudioService;
import me.catzy.invester.objects.lmStudio.LMStudioService.AICompletion;
import me.catzy.invester.objects.lmStudio.LMStudioService.AIMessage;
import me.catzy.invester.objects.lmStudio.LMStudioService.AIResponse;

@Service
public class MarketEventService extends GenericServiceImpl<MarketEvent, Long>{
	
	public MarketEventService(MarketEventRepository repository) {
		super(repository);
		this.repo = repository;
	}

	private MarketEventRepository repo;
	@Autowired ArticleRepository repoArticles;
	@Autowired LMStudioService serviceLM;
	
	private static final Logger logger = LoggerFactory.getLogger(MarketEventService.class);
	private static final int AI_PROCESSING_RETRIES = 3;
	
	@Scheduled(fixedRate = 1, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
	public void findAndProcessArticle() {
		List<Article> unprocessed = repoArticles.findByEventsIsEmptyAndContentIsNotNullOrderByTimestampDesc();
		
		for(int i = 0; i < unprocessed.size(); i++) {
			logger.info("Left to process: "+(unprocessed.size()-i));
			Article articleToProcess = unprocessed.get(i);
			processArticleTimed(articleToProcess);
		}
	}
	
	long tBooted = System.currentTimeMillis();
	long tSpentOnProcessing = 0;
	public void processArticleTimed(Article a) {
		long tStarted = System.currentTimeMillis();
		processArticle(a);
		long tPassed = System.currentTimeMillis()-tStarted;
		tSpentOnProcessing += tPassed;
		
		{
			long tPassedFromBoot = System.currentTimeMillis()-tBooted;
			double prcTimeInProcessing = (double)tSpentOnProcessing/(double)tPassedFromBoot*100.0;
			logger.info("Time spent on processing: "+prcTimeInProcessing+"%");
		}
	}
	
	public void processArticle(Article a) {
		logger.info("AI processing ID:" + a.getId() + ", title:"+a.getTitle());

		for (int i = 0; i < AI_PROCESSING_RETRIES; i++) {
			try {
				AIResponse mes = askChatbot(a.getContent(),a.getTimestamp());
				
				//inner TRY/CATCH so we can clean-up created mess
				try {
					for (MarketEvent me : mes.events) {
						logger.info("typ:" + (me.getType() == 0 ? "DOWN" : "UP") + ", prc:" + me.getImpactPrc() + ", chance: " + me.getImpactChance());

						me.setArticle(a);
						me = repo.save(me);
					}
					a.setProcessedTimestamp(new Timestamp(System.currentTimeMillis()));
					a = repoArticles.save(a);
					
					break;
				} catch (Exception e) {
					logger.error("AI processing FAILED article ID:" + a.getId() + ", retrying...");
					logger.error(mes.response);
					e.printStackTrace();
					
					//CLEANUP BEFORE ANOTHER TRY
					for(MarketEvent me : mes.events) {
						if(me.getId() == null) {
							continue;
						}
						try{repo.delete(me);} catch (Exception e2) {}
					}
				}
			} catch (Exception e) {
				logger.error("AI processing FAILED article ID:" + a.getId() + ", retrying...");
				e.printStackTrace();
			}

		}
	}
	
	//TODO: move prompt to a separate file
	public AIResponse askChatbot(String question, Timestamp timestamp) throws URISyntaxException, IOException, InterruptedException {
		AICompletion c = new AICompletion();
		
		AIMessage system = new AIMessage();
		system.role = "system";
		
		system.content = """
You are an AI expert in business problem-solving with unmatched expertise in market dynamics. You will receive a text message (article) to analyze.

Your task is to analyze this text and construct a response in JSON format. 
The response should be a array of objects.
Each object is representing influence on EURUSD CFD market (we are investing on the EUR side) and have to have following fields:

type: 0 (negative impact) or 1 (positive impact)
impactPrc: an integer from 0 to 100 representing the percentage fluctuation of the asset's value.
impactChance integer from 0 to 100 representing the chance this fluctuation will occur.
startTimestamp: A SQL TIMESTAMP representing the start of the influence in the format yyyy-MM-dd'T'HH:mm:ss.SSS±hh:mm. Specify the use of UTC offset instead of time zone abbreviations If the time zone is unknown, use Z to indicate UTC.
endTimestamp: A SQL TIMESTAMP representing the end of the influence, formatted similarly.
scream: a short expressive phrase (max 32 characters) in POLISH capturing a reason for this influence in a fun tone!.

Object can be both positive and negative. The generated objects will be further processed for charting purposes.
Object keys cannot be null.

EXAMPLE OF OUTPUT FORMATTING:
[
	{
      "type": 1,
      "impactPrc": 17,
      "impactChance": 7,
      "startTimestamp": "2025-02-25T16:06:00.000Z",
      "endTimestamp": "2025-02-26T00:00:00.000Z",
      "scream": "Weaker US Treasuries"
    },
    {
      "type": 0,
      "impactPrc": 7,
      "impactChance": 17,
      "startTimestamp": "2025-02-25T16:06:00.000Z",
      "endTimestamp": "2025-03-02T00:00:00.000Z",
      "scream": "Tariff Threats Sour Mood"
    }
]

Please make sure that you are formatting TIMESTAMP's yyyy-MM-dd'T'HH:mm:ss.SSSX It is important for you to replace X in provided format with correctl time zone DO NOT LEAVE X THERE!
When timezone is unknown replace X with Z (IT MEANS UTC)
This refined prompt ensures clarity, conciseness, and effectiveness in guiding the AI to process the news article accurately and generate the required JSON objects for analysis.

IMPORTANT That we want ONLY incluences on EURUSD market. Not other markets.
				
ARTICLE PUBLICATION DATE: {pubdate}
ARTICLE CONTENT:
				""";
		
		system.content = system.content.replace("{pubdate}", timestamp.toString());
		
		c.messages.add(system);
		
		AIMessage user = new AIMessage();
		user.role = "user";
		user.content = question;
		c.messages.add(user);
		
		return serviceLM.askAI(c);
	}
}
