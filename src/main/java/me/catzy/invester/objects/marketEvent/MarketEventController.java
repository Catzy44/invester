package me.catzy.invester.objects.marketEvent;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.catzy.invester.objects.marketEvent.MarketEventProcessingService.Estimation;

@RestController
@RequestMapping({ "/market_events"})
public class MarketEventController {
	@Autowired MarketEventRepository repo;
	@Autowired MarketEventProcessingService service;
	
	@GetMapping("/process")
	public void procArticles() throws Exception {
		service.findAndProcessArticle();
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	private static class EstimateRequestBody {
		Timestamp timestamp;
	}
	@PostMapping("/estimate")
	public Estimation estimate(@RequestBody EstimateRequestBody body) throws Exception {
		return service.getEstimationForaDay(body.timestamp);
	}
}
