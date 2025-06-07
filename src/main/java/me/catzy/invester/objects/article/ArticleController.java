package me.catzy.invester.objects.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.catzy.invester.generic.GenericController;
import me.catzy.invester.objects.marketEvent.MarketEvent;

@RestController
@RequestMapping({ "/articles"})
public class ArticleController extends GenericController<Article, Long> {
	@Autowired ArticleService service;
	@Autowired ArticleRepository repo;
	
	public ArticleController(ArticleService service) {
        super(service);
    }
	
	@GetMapping("/start")
	public void loadArticles() throws Exception {
		service.checkForAnyNews();
	}
	
	private static interface getEvents extends Article.vMarketEvents, MarketEvent.values {}
	@JsonView(getEvents.class)
	@GetMapping("/{id}/marketEvents")
	public List<MarketEvent> getEvents(@PathVariable long id) {
		return repo.findById(id).get().getEvents();
	}
}
