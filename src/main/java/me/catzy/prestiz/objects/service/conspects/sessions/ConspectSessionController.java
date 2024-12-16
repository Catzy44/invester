package me.catzy.prestiz.objects.service.conspects.sessions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.catzy.prestiz.generic.GenericController;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.service.conspects.Conspect;

@RestController
@RequestMapping({ "/service_conspect_session"})
public class ConspectSessionController extends GenericController<ConspectSession, Long> {
	public ConspectSessionController(ConspectSessionService service) {
        super(service);
    }
	
	@Autowired ConspectSessionService service;
	
	
	private interface getAll extends ConspectSession.values, 
	ConspectSession.conspect, Conspect.id,
	ConspectSession.instructor, Instruktor.id {};
	@JsonView({getAll.class})
	@GetMapping
	public ResponseEntity<List<ConspectSession>> getAll() {
		return ResponseEntity.ok(service.findAll());
	}
}
