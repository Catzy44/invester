package me.catzy.prestiz.objects.service.conspects.fields;

import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class ConspectFieldService extends GenericServiceImpl<ConspectField, Long> {
	public ConspectFieldService(ConspectFieldRepository repository) {
		super(repository);
	}
}
