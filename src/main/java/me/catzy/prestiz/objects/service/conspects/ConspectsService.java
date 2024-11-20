package me.catzy.prestiz.objects.service.conspects;

import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class ConspectsService extends GenericServiceImpl<Conspect, Long> {
	public ConspectsService(ConspectsRepository repository) {
		super(repository);
	}
}
