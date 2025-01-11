package me.catzy.prestiz.objects.instruktorzy;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.catzy.prestiz.generic.GenericController;
import me.catzy.prestiz.objects.service.conspect.category.ConspectCategory;
import me.catzy.prestiz.objects.service.conspects.Conspect;
import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@RestController
@RequestMapping({ "/instructors" })
@JsonView(Instruktor.values.class)
public class InstruktorzyController extends GenericController<Instruktor, Long> {
	public InstruktorzyController(InstruktorzyService service) {
		super(service);
	}

	@Autowired
	InstruktorzyService service;
	@Autowired
	InstruktorzyRepository repo;

	@JsonView({ Instruktor.values.class })
	@GetMapping({ "self" })
	public Instruktor getSelf() {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		Instruktor i = (Instruktor) a.getPrincipal();
		return i;
	}

	private static interface getAllowedCategories extends Instruktor.allowedServiceCategories, ConspectCategory.id {
	}

	@JsonView({ getAllowedCategories.class })
	@GetMapping({ "{id}/getAllowedServiceCategories" })
	public List<ConspectCategory> getAllowedCategories(@PathVariable("id") long id) {
		return service.findById(id).get().getAllowedServiceCategories();
	}

	@PostMapping
	public ResponseEntity<Instruktor> create(@RequestBody Instruktor entity) throws Exception {
		entity.setPassword(InstruktorzyService.generateAuthmeString(entity.getPassword()));
		return ResponseEntity.ok(service.save(entity));
	}

	@AllArgsConstructor
	@Getter
	@JsonView({getConspectTimes.class})
	public static class ConspectTimesDTO {
		public Conspect conspect;
		public double secondsIn;
		public Timestamp firstOpen;
		public Timestamp lastOpen;
	}
	
	private static interface getConspectTimes extends Conspect.id,ConspectField.values {}
	@GetMapping({ "{id}/conspect/times" })
	@JsonView({getConspectTimes.class})
	public List<ConspectTimesDTO> getConspectTimes(@PathVariable("id") int id) {
		return repo.getConspectTimes(repo.getById(id));
	}
	
	@AllArgsConstructor
	@Getter
	@JsonView({getConspectCounters.class})
	public static class ConspectCountersDTO {
		public ConspectField conspectField;
		public double filledQuestionsCount;
	}
	private static interface getConspectCounters extends ConspectField.id,ConspectField.conspect,Conspect.id {}
	@GetMapping({ "{id}/conspect/counters" })
	@JsonView({getConspectCounters.class})
	public List<ConspectCountersDTO> getConspectCounters(@PathVariable("id") int id) {
		return repo.getConspectFieldCounters(repo.getById(id));
	}
}
