package me.catzy.invester.objects.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.catzy.invester.generic.GenericController;

@RestController
@RequestMapping({ "/articles"})
public class ArticleController extends GenericController<Article, Long> {
	@Autowired ArticleService service;
	@Autowired ArticleRepository repo;
	
	public ArticleController(ArticleService service) {
        super(service);
    }
	
	/*private interface ConspectNorm extends Article.values,Article.conspectCategoryList,ConspectCategory.id {}
	@JsonView({ConspectNorm.class})
	@GetMapping
	public ResponseEntity getAll() {
		return ResponseEntity.ok(service.findAll());
	}*/
	
	@GetMapping("/start")
	public void loadArticles() throws Exception {
		service.loadArticles();
	}
	
	/*@GetMapping("/process")
	public void procArticles() throws Exception {
		service.processArticles();
	}*/
}
