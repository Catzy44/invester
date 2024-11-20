package me.catzy.prestiz.objects.service.conspects.fields.replies;

import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class ConspectFieldReplyService extends GenericServiceImpl<ConspectFieldReply, Long> {
	public ConspectFieldReplyService(ConspectFieldReplyRepository repository) {
		super(repository);
	}
}
