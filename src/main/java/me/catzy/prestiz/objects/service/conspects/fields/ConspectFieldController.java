package me.catzy.prestiz.objects.service.conspects.fields;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.catzy.prestiz.generic.GenericController;

@RestController
@RequestMapping({ "/service_conspect_field"})
public class ConspectFieldController extends GenericController<ConspectField, Long> {
	public ConspectFieldController(ConspectFieldService service) {
        super(service);
    }
}
