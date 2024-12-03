package me.catzy.prestiz.objects.service.conspects.fields;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.service.conspects.Conspect;
import me.catzy.prestiz.objects.service.conspects.ConspectRepository;
import me.catzy.prestiz.objects.service.conspects.fields.replies.ConspectFieldReply;
import me.catzy.prestiz.objects.service.conspects.fields.replies.ConspectFieldReplyRepository;

@Service
public class ConspectFieldService extends GenericServiceImpl<ConspectField, Long> {
	public ConspectFieldService(ConspectFieldRepository repository) {
		super(repository);
	}
	
	@Autowired ConspectRepository repoConspect;
	@Autowired ConspectFieldRepository repo;
	@Autowired ConspectFieldReplyRepository repoReplies;
	
	public List<ConspectFieldReply> findReplies(ConspectField f, Instruktor i) {
		return repoReplies.getInstructorReplies(f,i);
	}
	
	@Override
	public ConspectField save(ConspectField entity) {
		
		boolean isNewObject = (entity.getId() == null);
		if(isNewObject) {
			Conspect c = entity.getConspect();
			int maxSort = repoConspect.findMaxSortField(c);
			entity.setSort(maxSort+1);
		}
		
		return super.save(entity);
	}
	
	@Override
	public List<ConspectField> findAll() {
		return repo.findAllNotDeleted();
	}
}
