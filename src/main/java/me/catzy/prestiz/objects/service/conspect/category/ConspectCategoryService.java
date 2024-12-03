package me.catzy.prestiz.objects.service.conspect.category;

import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class ConspectCategoryService extends GenericServiceImpl<ConspectCategory, Long> {
	public ConspectCategoryService(ConspectCategoryRepository repository) {
		super(repository);
	}
}
