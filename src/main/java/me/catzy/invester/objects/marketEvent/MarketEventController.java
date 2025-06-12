package me.catzy.invester.objects.marketEvent;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.catzy.invester.generic.GenericController;
import me.catzy.invester.objects.marketEvent.MarketEventRepository.EstimationDTO;

@RestController
@RequestMapping({ "/marketEvents"})
public class MarketEventController extends GenericController<MarketEvent, Long>{
	public MarketEventController(MarketEventService service) {
		super(service);
		this.service = service;
	}
	@Autowired MarketEventRepository repo;
	private MarketEventService service;
	
	//manual
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
	public EstimationDTO estimate(@RequestBody EstimateRequestBody body) throws Exception {
		return repo.getDailyEstimation(body.timestamp);
	}
	
	@JsonView(MarketEvent.values.class)
	@GetMapping("/current")
	public List<MarketEvent> currentEvents() {
		return repo.getCurrentEvents();
	}
}
