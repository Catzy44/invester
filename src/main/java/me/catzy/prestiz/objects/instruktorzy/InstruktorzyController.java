package me.catzy.prestiz.objects.instruktorzy;

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

import me.catzy.prestiz.ToolBox;
import me.catzy.prestiz.generic.GenericController;
import me.catzy.prestiz.objects.service.conspect.category.ConspectCategory;

@RestController
@RequestMapping({"/instructors"})
@JsonView(Instruktor.values.class)
public class InstruktorzyController extends GenericController<Instruktor, Long> {
	public InstruktorzyController(InstruktorzyService service) {
        super(service);
    }
	
  @Autowired InstruktorzyService service;
  
  /*@GetMapping({"{id}"})
  public Instruktor getInstruktor(@PathVariable("id") int id, @RequestParam(required = false) String projection) {
    return service.byId(id);
  }*/
  
  @JsonView({Instruktor.values.class})
  @GetMapping
  public ResponseEntity<List<Instruktor>> getAll() {
    return ResponseEntity.ok(this.service.getAll());
  }
  
  /*@PatchMapping({"{id}"})
  private Instruktor updateStatusMultipleByIds(@PathVariable("id") int id, @RequestBody Map<Object, Object> list) throws Exception {
    return service.patch(id,list);
  }*/
  
  @JsonView({Instruktor.values.class})
  @GetMapping({"self"})
  public Instruktor getSelf() {
	  Authentication a = SecurityContextHolder.getContext().getAuthentication();
	  Instruktor i = (Instruktor) a.getPrincipal();
	  return i;
  }
  
  private static interface getAllowedCategories extends
  Instruktor.allowedServiceCategories,
  ConspectCategory.id{}
  @JsonView({getAllowedCategories.class})
  @GetMapping({"{id}/getAllowedServiceCategories"})
  public List<ConspectCategory> getAllowedCategories(@PathVariable("id") long id) {
	  return service.findById(id).get().getAllowedServiceCategories();
  }
  
  @PostMapping
  public ResponseEntity<Instruktor> create(@RequestBody Instruktor entity) throws Exception {
	entity.setPassword(ToolBox.generateAuthmeString(entity.getPassword()));
	return ResponseEntity.ok(service.save(entity));
  }
}
