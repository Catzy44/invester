package me.catzy.prestiz.objects.service.conspects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.catzy.prestiz.generic.GenericController;
import me.catzy.prestiz.objects.service.conspect.category.ConspectCategory;
import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@RestController
@RequestMapping({ "/service_conspect"})
public class ConspectController extends GenericController<Conspect, Long> {
	
	
	
	
	@Autowired ConspectsService service;
	
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
}
