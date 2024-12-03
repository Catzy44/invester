package me.catzy.prestiz.objects.service.conspects.fields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.catzy.prestiz.generic.GenericController;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;

@RestController
@RequestMapping({ "/service_conspect_field"})
public class ConspectFieldController extends GenericController<ConspectField, Long> {
	public ConspectFieldController(ConspectFieldService service) {
        super(service);
    }
	@Autowired ConspectFieldService service;
	
	@GetMapping({"{id}/replies"})
	public Object[] getConspectFieldReplies(@PathVariable Long id) {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		Instruktor i = (Instruktor) a.getPrincipal();
		
		return service.findReplies(service.findById(id).get(), i).toArray();
	}
}
