package me.catzy.prestiz.objects.miasta;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/miasta"})
public class MiastaController {
  @Autowired
  MiastaService service;
  
  @GetMapping
  public List<Miasto> getAllCities() {
    return this.service.getAll();
  }
}
