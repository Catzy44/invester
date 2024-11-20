package me.catzy.prestiz.objects.instruktorzy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/instructors"})
public class InstruktorzyController {
  @Autowired
  InstruktorzyService service;
  
  @GetMapping({"{id}"})
  public Instruktor getInstruktor(@PathVariable("id") int id, @RequestParam(required = false) String projection) {
    return service.byId(id);
  }
  
  @GetMapping
  public List<Instruktor> getAll() {
    return this.service.getAll();
  }
  
  @PatchMapping({"{id}"})
  private Instruktor updateStatusMultipleByIds(@PathVariable("id") int id, @RequestBody Map<Object, Object> list) throws Exception {
    return service.patch(id,list);
  }
  
  @GetMapping({"self"})
  public Instruktor getSelf() {
	  Authentication a = SecurityContextHolder.getContext().getAuthentication();
	  Instruktor i = (Instruktor) a.getPrincipal();
	  return i;
  }
}
