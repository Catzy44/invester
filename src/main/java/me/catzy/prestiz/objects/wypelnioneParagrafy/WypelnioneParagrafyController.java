package me.catzy.prestiz.objects.wypelnioneParagrafy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/wypelnione_paragrafy"})
public class WypelnioneParagrafyController {
  @Autowired
  WypelnioneParagrafyService service;
  
  @GetMapping({"/{id}"})
  public WypelnionyParagraf searchByName(@PathVariable int toOrder, @PathVariable int orderAfter) {
    return null;
  }
}
