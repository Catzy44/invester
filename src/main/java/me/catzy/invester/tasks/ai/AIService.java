package me.catzy.invester.tasks.ai;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Service
public class AIService {
	public static class Estimation {
		public int impactGood;
		public int impactBad;
		public String scream;
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
	
	public Estimation askChatbot(String question) throws URISyntaxException, IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		
		Completion c = new Completion();
		
		Message system = new Message();
		system.role = "system";
		system.content = "You are the world’s most insightful business solutions AI with unmatched expertise in market dynamics. "
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
				+ "Do not include any special characters in the scream.";
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
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Response res = objectMapper.readValue(response.body(), Response.class);
        
        String reply = res.choices.get(0).message.content;
        String replyNoThinking = reply.split("</think>\n\n")[1];
        
        replyNoThinking = replyNoThinking.replace("```json", "");
        replyNoThinking = replyNoThinking.replace("```", "");
        
        Estimation es = objectMapper.readValue(replyNoThinking, Estimation.class);
        
        return es;
	}
}
