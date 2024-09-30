package me.catzy.prestiz.objects.zajecia;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/classes", "/classes"})
public class ZajeciaController {
  /*@Autowired ZajeciaService service;
  @Autowired ZajeciaRepository repo;
  
  class PatchPresenceDO {
	  private int id;
	  private int newPresence;
  }
  @PostMapping({"{id}/patchPresence"})
  private Zajecia patchPresence(@RequestBody PatchPresenceDO ppDO) throws JsonMappingException, UserException {
	  Zajecia z = repo.getReferenceById(ppDO.id);
	  z.
  }
  
  @GetMapping({"{id}/classPricesForAllMonths"})
  public float[] getClassPricesForAllMonths(@PathVariable("id") int id) {
    return null;
  }*/
}
