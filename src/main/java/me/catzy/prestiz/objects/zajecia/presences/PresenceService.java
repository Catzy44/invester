package me.catzy.prestiz.objects.zajecia.presences;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.zajecia.Zajecia;

@Service
public class PresenceService {
	@Autowired
	PresenceRepository repo;
	@Autowired
	private ObjectMapper objectMapper;

	public Presence findByClassAndAttender(Uczestnik u, Zajecia z) {
		return this.repo.findByClassAndAttender(u, z);
	}

	public Presence patch(Map<Object, Object> map) throws UserException, JsonMappingException {
		int id = -1;
		for (Object k_ : map.keySet()) {
			String k = (String) k_;
			if (k.equals("id")) {
				id = ((Integer) map.get(k)).intValue();
				break;
			}
		}
		if (id == -1)
			throw new UserException("id key not found within object", "meow!");
		Presence obj = this.repo.getById(id);
		this.objectMapper.updateValue(obj, map);
		return save(obj);
	}

	public Presence save(Presence c) {
		return (Presence) this.repo.save(c);
	}
}
