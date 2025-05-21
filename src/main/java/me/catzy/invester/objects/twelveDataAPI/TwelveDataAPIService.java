package me.catzy.invester.objects.twelveDataAPI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Setter;

@Service
public class TwelveDataAPIService {
	private ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
		    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;
		    
	private static HttpClient client = HttpClient.newHttpClient();
	
	public static void main(String[] args) {
		try {
			LocalDate end = LocalDate.now();
			LocalDate start = end.minusWeeks(1);
			APIResponse api = new TwelveDataAPIService().askAPI("1h",start,end);
			System.out.println(api.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
	}
	
	public String formatDateForAPI(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	//800 API CALLS!
	public APIResponse askAPI(String interval, LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException, URISyntaxException {
		HttpRequest request = HttpRequest.newBuilder()
	        	.version(HttpClient.Version.HTTP_1_1)
	            .uri(new URI("https://api.twelvedata.com/time_series?symbol=EUR/USD&interval="+interval+"&start_date="+formatDateForAPI(startDate)+"&end_date="+formatDateForAPI(endDate)+"&apikey=e838323726454a30b11fa06845269edb"))
	            .GET()
	            .header("Content-Type", "application/json; charset=utf-8")
	            .header("User-Agent", "JavaHttpClient/1.0")
	            .header("Accept", "*/*")
	            .timeout(Duration.ofMinutes(1))
	            .build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		APIResponse res = objectMapper.readValue(response.body(), APIResponse.class);
		return res;
	}
	
	@Setter
	public static class APIResponse {
		Meta meta;
		List<Value> values;
		String status;
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("symbol:"+meta.symbol+",interval:"+meta.interval+"\n");
			values.stream().forEach(v->{
				sb.append(v.toString()+"\n");
			});
			return sb.toString();
		}
	}
	@Setter
	static class Meta {
		String symbol;
		String interval;
		String currency_base;
		String currency_quote;
		String type;
	}
	@Setter
	static class Value {
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime datetime;	
		float open;
		float high;
		float low;
		float close;
		
		@Override
		public String toString() {
			return "datetime:"+datetime.toString()+",open:"+open+",close:"+close+",high:"+high+",low:"+low;
		}
	}
}

