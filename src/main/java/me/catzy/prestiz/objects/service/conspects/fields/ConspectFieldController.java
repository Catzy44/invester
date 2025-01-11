package me.catzy.prestiz.objects.service.conspects.fields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.catzy.prestiz.generic.GenericController;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.service.conspects.fields.replies.ConspectFieldReply;

@RestController
@RequestMapping({ "/service/conspect/field"})
public class ConspectFieldController extends GenericController<ConspectField, Long> {
	public ConspectFieldController(ConspectFieldService service) {
        super(service);
    }
	@Autowired ConspectFieldService service;
	
	public static class ConspectInstructorDTO {
		public Instruktor instructor;
	}
	@PostMapping({"{id}/replies"})
	@JsonView({ConspectFieldReply.values.class})
	public Object getConspectFieldReplies(@PathVariable("id") Long id, @RequestBody(required=false) ConspectInstructorDTO dto) {
		Instruktor i = dto == null ? null : dto.instructor;
		if(i == null) {
			Authentication a = SecurityContextHolder.getContext().getAuthentication();
			i = (Instruktor) a.getPrincipal();
		}
		return service.findReplies(service.findById(id).get(), i).toArray();
	}
	@GetMapping({"{id}/replies"})
	@JsonView({ConspectFieldReply.values.class})
	public Object getConspectFieldRepliesPOST(@PathVariable("id") Long id) {
		return getConspectFieldReplies(id,null);
	}
}
