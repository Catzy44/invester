package me.catzy.prestiz.objects.sms_templates;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/sms_templates"})
public class SMSTemplateController {
  @Autowired
  private SMSTemplateService service;
  
  @Autowired
  private SMSTemplateRepository repo;
  
  @PatchMapping
  private SMSTemplate updateStatusMultipleByIds(@RequestBody Map<Object, Object> list) throws Exception {
    return this.service.patch(list);
  }
  
  @PostMapping
  public SMSTemplate receivedSMSasdf(@RequestBody SMSTemplate sms) {
    return this.service.save(sms);
  }
  
  @GetMapping({"/{id}"})
  public SMSTemplate getUnreadSMSCount() {
    return null;
  }
  
  @DeleteMapping({"/{id}"})
  public void deleteTemplate(@PathVariable("id") int id) {
    this.repo.delete(this.repo.getById(id));
  }
  
  @GetMapping({"/type/{type}"})
  public List<SMSTemplate> getAllByType(@PathVariable("type") int type) {
    return this.repo.getByType(type);
  }
}
