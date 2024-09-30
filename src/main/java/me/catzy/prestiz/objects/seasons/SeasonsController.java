package me.catzy.prestiz.objects.seasons;

import java.util.List;
import me.catzy.prestiz.ToolBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/seasons"})
public class SeasonsController {
  @Autowired
  private SeasonsService service;
  
  @Autowired
  private ToolBox box;
  
  @GetMapping
  public List<Season> getAll() {
    return this.service.getAll();
  }
  
  @GetMapping({"{id}/attenders"})
  public ResponseEntity<?> getAllAttenders(@PathVariable int id, @RequestParam(required = false) String projection) {
    Season sea = this.service.getById(id);
    return new ResponseEntity(this.box.projectList(sea.getAttenders(), projection), (HttpStatusCode)HttpStatus.OK);
  }
  
  @GetMapping({"{id}/places"})
  public ResponseEntity<?> getAllPlaces(@PathVariable int id, @RequestParam(required = false) String projection) {
    Season sea = this.service.getById(id);
    return new ResponseEntity(this.box.projectList(sea.getPlaces(), projection), (HttpStatusCode)HttpStatus.OK);
  }
}
