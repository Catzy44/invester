package me.catzy.prestiz.objects.sesje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/sesje"})
public class SesjeController {
  @Autowired
  SesjeService service;
  
  @GetMapping({"/find/{uid}"})
  public Sesja aktywnyFormularz(@PathVariable("uid") String uid) throws Exception {
    return this.service.findActiveSessionByKey(uid);
  }
}
