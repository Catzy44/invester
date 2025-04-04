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
import me.catzy.invester.objects.marketEvent.MarketEventRepository.EstimationDTO;

@RestController
@RequestMapping({ "/marketEvents"})
public class MarketEventController {
	@Autowired MarketEventRepository repo;
	@Autowired MarketEventService service;
	
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
	
	@JsonView({MarketEvent.Fields.class})
	@GetMapping("/all")
	public List<MarketEvent> getAll() {
		return repo.findAll();
	}
	@JsonView({MarketEvent.Fields.class})
	@PostMapping("/all")
	public List<MarketEvent> getAllP() {
		return repo.findAll();
	}
}
