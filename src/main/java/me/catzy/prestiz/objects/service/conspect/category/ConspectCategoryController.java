package me.catzy.prestiz.objects.service.conspect.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.catzy.prestiz.generic.GenericController;

@RestController
@RequestMapping({ "/service_conspect_category"})
public class ConspectCategoryController extends GenericController<ConspectCategory, Long> {
	@Autowired ConspectCategoryService service;
	
	public ConspectCategoryController(ConspectCategoryService service) {
        super(service);
    }
}
