package me.catzy.prestiz.objects.zajecia.presences;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;

import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.zajecia.ZajeciaService;

@RestController
@RequestMapping({ "/presences", "/presences" })
public class PresenceController {
	@Autowired PresenceService service;
	@Autowired UczestnicyService serviceAtt;
	@Autowired ZajeciaService serviceClass;

	@PatchMapping
	private Presence updateById(@RequestBody Map<Object, Object> list) throws Exception {
		return this.service.patch(list);
	}

	static class InsertNewDO {
		public int attenderId;
		public int presence;
		public int classId;
	}

	@PostMapping
	private Presence insert(@RequestBody InsertNewDO newdo) throws JsonMappingException, UserException {
		Presence p = new Presence();
		//p.setAttender(serviceAtt.byId(newdo.attenderId));
		p.setClasses(serviceClass.byId(newdo.classId));
		p.setPresence(newdo.presence);
		
		return service.save(p);
	}
}
