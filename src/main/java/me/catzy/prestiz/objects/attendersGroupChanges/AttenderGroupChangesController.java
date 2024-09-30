package me.catzy.prestiz.objects.attendersGroupChanges;

import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonMappingException;

import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.zajecia.Zajecia;
import me.catzy.prestiz.objects.zajecia.ZajeciaService;
import me.catzy.prestiz.objects.zajecia.presences.Presence;

@RestController
@RequestMapping({"/groupChanges"})
@Transactional
public class AttenderGroupChangesController {
  @Autowired
  AttenderGroupChangesService service;
  @Autowired
  ZajeciaService serviceClasses;
  
  @GetMapping({"{id}/classPricesForAllMonths"})
  public float[] getClassPricesForAllMonths(@PathVariable("id") int id) {
    return null;
  }
  
  private static interface getClassesByAGC extends Presence.vPresenceAttender,Presence.vPresenceData,Presence.vPresenceId,Presence.vPresencePresence,
  Zajecia.vZajeciaId,Zajecia.vZajeciaData,Zajecia.vZajeciaPresences,
  Uczestnik.vUczestnikId{}
  @JsonView({getClassesByAGC.class})
  @GetMapping({"{id}/classes"})
  public Zajecia[] getClassesByAGC(@PathVariable("id") int id) {
	  AttenderGroupChange agc = service.byId(id);
	  Hibernate.initialize(agc);
	  return serviceClasses.getClassesByAGC(agc);
  }
  
  @PatchMapping
  private AttenderGroupChange updateById(@RequestBody Map<Object, Object> list) throws Exception {
    return this.service.patch(list);
  }
  
  @PostMapping
  private AttenderGroupChange insert(@RequestBody Map<Object, Object> list) throws JsonMappingException, UserException {
    return this.service.make(list);
  }
  
  @DeleteMapping({"{id}"})
  private void insert(@PathVariable("id") int id) {
    this.service.delete(this.service.byId(id));
  }
}
