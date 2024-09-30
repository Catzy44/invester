package me.catzy.prestiz.objects.sms_numbers;

import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/SMSNumber"})
public class SMSNumberController {
  @Autowired
  SMSNumberService service;
  
  @Autowired
  UczestnicyService serviceUczestnicy;
  
  @PostMapping
  public SMSNumber update(@RequestBody SMSNumber sms) throws Exception {
    return this.service.save(sms);
  }
}
