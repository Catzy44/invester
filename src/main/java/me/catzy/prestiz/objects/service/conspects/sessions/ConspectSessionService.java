package me.catzy.prestiz.objects.service.conspects.sessions;

import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class ConspectSessionService extends GenericServiceImpl<ConspectSession, Long> {
	public ConspectSessionService(ConspectSessionReporitory repository) {
		super(repository);
	}
}
