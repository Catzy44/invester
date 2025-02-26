package me.catzy.invester.objects.market_event;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.catzy.invester.objects.article.Article;
import me.catzy.invester.objects.article.ArticleRepository;

@Service
public class MarketEventService {
	
	@Autowired MarketEventRepository repo;
	@Autowired ArticleRepository repoArticles;
	private static final Logger logger = LoggerFactory.getLogger(MarketEventService.class);
	
	@Scheduled(fixedRate = 3, initialDelay = 0, timeUnit = TimeUnit.MINUTES)
	public void findAndProcessArticle() {
		List<Article> unprocessed = repoArticles.findByEventsIsEmpty();
		
		for(Article a : unprocessed) {
			processArticle(a);
		}
	}
	
	public void processArticle(Article a) {
		logger.info("AI processing ID:" + a.getId() + ", title:"+a.getTitle());

		for (int i = 0; i < 3; i++) {
			try {

				AIResponse mes = askChatbot(a.getContent(),a.getTimestamp());
				
				try {
					for (MarketEvent me : mes.events) {
						logger.info("typ:" + (me.getType() == 0 ? "DOWN" : "UP") + ", value:" + me.getImpact());

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
	
	
	public AIResponse askChatbot(String question, Timestamp timestamp) throws URISyntaxException, IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		
		Completion c = new Completion();
		
		Message system = new Message();
		system.role = "system";
		/*system.content = "You are the world’s most insightful business solutions AI with unmatched expertise in market dynamics. "
				+ "You will be provided with a news text. "
				+ "Your task is to analyze it thoroughly and determine two coefficients: "
				+ "impactGood, representing the likelihood that the news will have a positive impact on the EURUSD market"
				+ ", and impactBad, representing the likelihood that the news will have a negative impact on the EURUSD market. "
				+ "Both coefficients must be integers from 1 to 10, where 1 means very low probability and 10 means very high probability. "
				+ "Your evaluations must be realistic and based on a careful analysis of the news and actual market conditions. "
				+ "Your final response must consist solely of a JSON object with exactly three keys: \"impactGood\", \"impactBad\", and \"scream\". "
				+ "The first two keys map to integer values, and the \"scream\" key should contain a brief string (up to 32 characters) reserved for your short exclamation and expression. "
				+ "Do not include any extra commentary, formatting, or text. "
				+ "For example, your output should look exactly like: {\"impactGood\": 8, \"impactBad\": 3, \"scream\": \"Paws up! Check ETH!\"} "
				+ "Provide only the required JSON object in your final answer."
				+ "Do not include any special characters in the scream.";*/
		
		/*
		 * dateStart: a SQL TIMESTAMP representing the start of the influence
dateEnd: a SQL TIMESTAMP representing the end of the influence
		 * */
		system.content = """
You are an AI expert in business problem-solving with unmatched expertise in market dynamics. You will receive a text message (article) to analyze.

Your task is to analyze this text and construct a response in JSON format. 
The response should be a array of objects.
Each object is representing influence on the EURUSD market and have to have following fields:

type: 0 (negative impact) or 1 (positive impact)
impact: an integer from 0 to 10 indicating the weight of the influence
startTimestamp: A SQL TIMESTAMP representing the start of the influence in the format yyyy-MM-dd'T'HH:mm:ss.SSS±hh:mm. Specify the use of UTC offset instead of time zone abbreviations If the time zone is unknown, use Z to indicate UTC.
endTimestamp: A SQL TIMESTAMP representing the end of the influence, formatted similarly.
scream: a short phrase (up to 32 characters) capturing a /*brief outcry or expression*/your expression.

Object can be both positive and negative. The generated objects will be further processed for charting purposes.
Object keys cannot be null.

EXAMPLE OF OUTPUT FORMATTING:
[
	{
      "type": 1,
      "impact": 7,
      "startTimestamp": "2025-02-25T16:06:00.000Z",
      "endTimestamp": "2025-02-26T00:00:00.000Z",
      "scream": "Weaker US Treasuries"
    },
    {
      "type": 0,
      "impact": 8,
      "startTimestamp": "2025-02-25T16:06:00.000Z",
      "endTimestamp": "2025-03-02T00:00:00.000Z",
      "scream": "Tariff Threats Sour Mood"
    }
]

Please make sure that you are formatting TIMESTAMP's yyyy-MM-dd'T'HH:mm:ss.SSSX It is important for you to replace X in provided format with correctl time zone DO NOT LEAVE X THERE!
When timezone is unknown replace X with Z (IT MEANS UTC)
This refined prompt ensures clarity, conciseness, and effectiveness in guiding the AI to process the news article accurately and generate the required JSON objects for analysis.
				
ARTICLE PUBLICATION DATE: {pubdate}
ARTICLE CONTENT:
				""";
		
		system.content = system.content.replace("{pubdate}", timestamp.toString());
		
		c.messages.add(system);
		
		Message user = new Message();
		user.role = "user";
		user.content = question;
		c.messages.add(user);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper
		      .writerWithDefaultPrettyPrinter()
		      .writeValueAsString(c);
		
        HttpRequest request = HttpRequest.newBuilder()
        	.version(HttpClient.Version.HTTP_1_1)
            .uri(new URI("http://127.0.0.1:1234/api/v0/chat/completions"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .timeout(Duration.ofSeconds(60))
            .header("Content-Type", "application/json; charset=utf-8")
            .header("User-Agent", "JavaHttpClient/1.0")
            .header("Accept", "*/*")
            .timeout(Duration.ofSeconds(60*10))
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Response res = objectMapper.readValue(response.body(), Response.class);
        
        String reply = res.choices.get(0).message.content;
        String replyNoThinking = reply.split("</think>\n\n")[1];
        
        replyNoThinking = replyNoThinking.replace("```json", "");
        replyNoThinking = replyNoThinking.replace("```", "");
        
        MarketEvent[] me = objectMapper.readValue(replyNoThinking, MarketEvent[].class);

        return new AIResponse(me,reply);
	}
	
	@AllArgsConstructor
	public static class AIResponse {
		public MarketEvent[] events;
		public String response;
	}
	
	@Getter
	public static class Message {
		public String role = null;
		public String content = null;
	}
	@Getter
	public static class Completion {
		public String model = "deepseek-r1-distill-qwen-14b";
		public float temperature = 0.7f;
		public boolean stream = false;
		public int max_tokens = -1;
		public  List<Message> messages = new ArrayList<Message>();
	}
	
	public static class Response {
		public String id;
		public String object;
		public long created;
		public String model;
		public  List<Choice> choices = new ArrayList<Choice>();
		public Usage usage;
		public Stats stats;
		public ModelInfo model_info;
		public Runtime runtime;
	}
	public static class Choice {
		public int index;
		public Object logprobs;
		public String finish_reason;
		public Message message;
	}
	public static class Usage {
		public int prompt_tokens;
		public int completion_tokens;
		public int total_tokens;
	}
	public static class Stats {
		public int tokens_per_second;
		public float time_to_first_token;
		public int generation_time;
		public String stop_reason;
	}
	public static class ModelInfo {
		public String arch;
		public String quant;
		public String format;
		public int context_length;
	}
	public static class Runtime {
		public String name;
		public String version;
		public List<String> supported_formats = new ArrayList<String>();
	}
}
