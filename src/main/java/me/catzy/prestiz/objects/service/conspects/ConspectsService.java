package me.catzy.prestiz.objects.service.conspects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class ConspectsService extends GenericServiceImpl<Conspect, Long> {
	public ConspectsService(ConspectRepository repository) {
		super(repository);
	}
	
	@Autowired ConspectRepository repo;
	
	@Override
	public List<Conspect> findAll() {
		return repo.findAllNotDeleted();
	}
}
