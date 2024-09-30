package me.catzy.prestiz.objects.umowy;

import java.util.List;
import me.catzy.prestiz.objects.wypelnioneParagrafy.WypelnioneParagrafyService;
import me.catzy.prestiz.objects.wypelnioneParagrafy.WypelnionyParagraf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/umowy"})
public class UmowyController {
  @Autowired
  UmowyService service;
  
  @Autowired
  WypelnioneParagrafyService serviceWypPar;
  
  @GetMapping({"/{id}/wypelnioneParagrafy"})
  public List<WypelnionyParagraf> searchByNahjme(@PathVariable int id) {
    return this.service.byId(id).getWypelnioneParagrafy();
  }
  
  @GetMapping({"/{id}"})
  public Umowa searchBydghfName(@PathVariable int id) {
    return this.service.byId(id);
  }
}
