package me.catzy.prestiz.objects.service.conspects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.catzy.prestiz.generic.GenericController;

@RestController
@RequestMapping({ "/service_conspect"})
public class ConspectsController extends GenericController<Conspect, Long> {
	private interface ConspectRender extends Conspect.ConspectFull, Conspect.ConspectFields{}
	
	@Autowired ConspectsService service;
	
	public ConspectsController(ConspectsService service) {
        super(service);
    }
	
	
	@JsonView(ConspectRender.class)
	@GetMapping({"render"})
	public List<Conspect> render() {
		return service.findAll();
	}
}
