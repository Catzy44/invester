package me.catzy.prestiz.objects.service.conspects.fields.replies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.catzy.prestiz.generic.GenericController;

@RestController
@RequestMapping({ "/service/conspect/field/reply"})
public class ConspectFieldReplyController extends GenericController<ConspectFieldReply, Long> {
	public ConspectFieldReplyController(ConspectFieldReplyService service) {
        super(service);
    }
	@Autowired ConspectFieldReplyRepository repo;
}
