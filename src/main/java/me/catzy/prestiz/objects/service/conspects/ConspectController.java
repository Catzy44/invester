package me.catzy.prestiz.objects.service.conspects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.catzy.prestiz.generic.GenericController;
import me.catzy.prestiz.objects.instruktorzy.InstruktorzyService;
import me.catzy.prestiz.objects.service.conspect.category.ConspectCategory;
import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@RestController
@RequestMapping({ "/service/conspect"})
public class ConspectController extends GenericController<Conspect, Long> {
	@Autowired ConspectsService service;
	@Autowired InstruktorzyService serviceInst;
	@Autowired ConspectRepository repo;
	
	public ConspectController(ConspectsService service) {
        super(service);
    }
	
	
	private interface ConspectRender extends Conspect.values, 
	Conspect.conspectFieldList, 
	ConspectField.values,
	Conspect.conspectCategoryList,
	ConspectCategory.id{}
	@JsonView(ConspectRender.class)
	@GetMapping({"allWithFields"})
	public List<Conspect> render() {
		return service.findAll();
	}
	
	private interface getById extends Conspect.values, 
	Conspect.conspectFieldList, 
	ConspectField.values,
	Conspect.conspectCategoryList,
	ConspectCategory.id{}
	@JsonView(getById.class)
	@GetMapping({"{id}"})
	@Override
	public ResponseEntity getById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.findById(id).get());
	}
	
	private interface ConspectNorm extends Conspect.values,Conspect.conspectCategoryList,ConspectCategory.id {}
	@JsonView({ConspectNorm.class})
	@GetMapping
	public ResponseEntity getAll() {
		return ResponseEntity.ok(service.findAll());
	}

	private static interface getRepliesByConspController extends ConspectField.values {}
	@JsonView({getRepliesByConspController.class})
	@GetMapping("{id}/questions")
	public ResponseEntity getQuestionFields(@PathVariable("id") Long id) {
		return ResponseEntity.ok(repo.getQuestionFields(service.findById(id).get()));
	}
}
