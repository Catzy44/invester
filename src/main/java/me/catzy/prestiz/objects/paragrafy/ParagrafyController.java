package me.catzy.prestiz.objects.paragrafy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/paragrafy"})
public class ParagrafyController {
  @Autowired
  ParagrafyService service;
  
  @GetMapping({"/{toOrder}/order_after/{orderAfter}"})
  public void searchByName(@PathVariable int toOrder, @PathVariable int orderAfter) {
    this.service.orderAfter(this.service.byId(toOrder), this.service.byId(orderAfter));
  }
}
