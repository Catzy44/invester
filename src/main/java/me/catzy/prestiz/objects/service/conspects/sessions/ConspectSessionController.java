package me.catzy.prestiz.objects.service.conspects.sessions;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.catzy.prestiz.generic.GenericController;

@RestController
@RequestMapping({ "/service_conspect_session"})
public class ConspectSessionController extends GenericController<ConspectSession, Long> {
	public ConspectSessionController(ConspectSessionService service) {
        super(service);
    }
}
