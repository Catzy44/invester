package me.catzy.prestiz.objects.zajecia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.catzy.prestiz.objects.attendersGroupChanges.AttenderGroupChange;

@Service
@Transactional
public class ZajeciaService {
	@Autowired
	ZajeciaRepository repo;

	@Cacheable(value = { "classesListFromInactiveAGCs" }, key = "#agc", condition = "#agc.active == false")
	public Zajecia[] getClassesByAGCCache(AttenderGroupChange agc) {
		return this.repo.getClassesByAGC(agc.getDateJoined(), agc.getDateLeft(), agc.getGroup(), (agc.getAttender().getDateJoined() != null) ? agc.getAttender().getDateJoined() : agc.getAttender().getDateAdded(), agc.getAttender());
	}

	public Zajecia[] getClassesByAGC(AttenderGroupChange agc) {
		return this.repo.getClassesByAGC(agc.getDateJoined(), agc.getDateLeft(), agc.getGroup(), (agc.getAttender().getDateJoined() != null) ? agc.getAttender().getDateJoined() : agc.getAttender().getDateAdded(), agc.getAttender());
	}

	public Zajecia byId(int id) {
		return repo.getById(id);
	}
}
