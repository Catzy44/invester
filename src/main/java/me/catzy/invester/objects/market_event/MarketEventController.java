package me.catzy.invester.objects.market_event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/market_events"})
public class MarketEventController {
	@Autowired MarketEventRepository repo;
	@Autowired MarketEventService service;
	
	@GetMapping("/process")
	public void procArticles() throws Exception {
		service.findAndProcessArticle();
	}
}
