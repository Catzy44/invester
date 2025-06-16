package me.catzy.invester.objects.lmStudio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.catzy.invester.Utils;
import me.catzy.invester.objects.marketEvent.MarketEvent;

@Service
public class LMStudioService {
	private static ObjectMapper objectMapper = new ObjectMapper();
	private static HttpClient client = HttpClient.newHttpClient();
	private static final Logger logger = LoggerFactory.getLogger(LMStudioService.class);
	
	boolean LMStudioWaked = false;
	private void wakeLMStudioIfNeeded() throws IOException, InterruptedException {
		if(LMStudioWaked) {
			return;
		}
		logger.info("starting LMS...");
		Process p = new ProcessBuilder("lms","server","start").start();
		Utils.dumpUntilExahausted(p.getInputStream());
		Utils.dumpUntilExahausted(p.getErrorStream());
		p.waitFor();
		Thread.sleep(1000);
		
		//to be sure
		logger.info("unloading old LLM model...");
		p = new ProcessBuilder("lms","unload","kot").start();
		//Utils.dumpUntilExahausted(p.getInputStream());
		//Utils.dumpUntilExahausted(p.getErrorStream());
		p.waitFor();
		Thread.sleep(1000);
		
		logger.info("loading LLM model...");
		p = new ProcessBuilder("lms","load","14B","--identifier","kot").start();
		Utils.dumpUntilExahausted(p.getInputStream());
		Utils.dumpUntilExahausted(p.getErrorStream());
		p.waitFor();
		Thread.sleep(1000);
		
		logger.info("LMS up and running!");
		
		LMStudioWaked = true;
		
	}
	
	public AIResponse askAI(AICompletion c) throws URISyntaxException, IOException, InterruptedException {
		wakeLMStudioIfNeeded();
		
		// convert AIMessage DTO into JSON String
		String requestBody = objectMapper
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(c);

		//web request to LMStudio (chooosen AI model has to be started first through GUI)
		HttpRequest request = HttpRequest.newBuilder()
	        	.version(HttpClient.Version.HTTP_1_1)
	            .uri(new URI("http://127.0.0.1:1234/api/v0/chat/completions"))
	            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	            .header("Content-Type", "application/json; charset=utf-8")
	            .header("User-Agent", "JavaHttpClient/1.0")
	            .header("Accept", "*/*")
	            .timeout(Duration.ofMinutes(20))
	            .build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		// convert LMStudioAPI JSON String response into LMStudioAPIResponse DTO
		LMStudioAPIResponse res = objectMapper
				.readValue(response.body(), LMStudioAPIResponse.class);

		// try to parse AI response !THROWS!
		return res.tryToParseResponse();
	}
	
	@AllArgsConstructor
	public static class AIResponse {
		public MarketEvent[] events;
		public String response;
	}
	
	@Getter
	public static class AIMessage {
		public String role = null;
		public String content = null;
	}
	
	/*IM LEAVING SETTINGS THERE - ITS A STATIC CLASS SO ITS NOT WRONG*/
	@Getter
	public static class AICompletion {
		public String model = "deepseek-r1-distill-qwen-14b";
		public float temperature = 0.7f;
		public boolean stream = false;
		public int max_tokens = -1;
		public  List<AIMessage> messages = new ArrayList<AIMessage>();
	}
	
	/*LM STUDIO API FOR THE SAKE OF JACKSON*/
	public static class LMStudioAPIResponse {
		public String id;
		public String object;
		public long created;
		public String model;
		public  List<Choice> choices = new ArrayList<Choice>();
		public Usage usage;
		public Stats stats;
		public ModelInfo model_info;
		public LLMRuntime runtime;
		
		public AIResponse tryToParseResponse() throws JsonMappingException, JsonProcessingException {
			String rnt = getReplyNoThinking();
			
			rnt = rnt.replace("```json", "");
			rnt = rnt.replace("```", "");
			
			MarketEvent[] events = objectMapper.readValue(rnt, MarketEvent[].class);
	        
	        return new AIResponse(events,rnt);
		}
		
		public String getReplyNoThinking() {
			String reply = choices.get(0).message.content;
	        return reply.split("</think>\n\n")[1];
		}
	}
	public static class Choice {
		public int index;
		public Object logprobs;
		public String finish_reason;
		public AIMessage message;
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
	public static class LLMRuntime {
		public String name;
		public String version;
		public List<String> supported_formats = new ArrayList<String>();
	}
}
