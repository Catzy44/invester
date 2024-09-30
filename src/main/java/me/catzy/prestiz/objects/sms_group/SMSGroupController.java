package me.catzy.prestiz.objects.sms_group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;

@RestController
@RequestMapping({"/sms_groups"})
public class SMSGroupController {
  @Autowired
  SMSGroupService service;
  
  @Autowired
  UczestnicyService serviceUczestnicy;
  
  /* send SMS to selected attenders and group em!*/
  @JsonView({vsendSMSMulti.class})
  @PostMapping({"/send"})
  public SMSGroup sendSMSMulti(@RequestBody sendSMSMultiRB sms) throws Exception {
    return this.service.sendMultiSMS(this.serviceUczestnicy.getByIdsIn(sms.getUserIds()), sms.getMessage(), sms.getFilters());
  }
  
  @Data
  static class sendSMSMultiRB {
    private int[] userIds;
    private String message;
    private Filters filters;
    
    @Data
    static class Filters {
      private FilterAge age;
      private FilterSex sex;
      
      @Data
      static class FilterAge {
        private boolean enabled;
        private int from;
        private int to;
      }
      
      @Data
      static class FilterSex {
        private boolean enabled;
        private int value;
      }
    }
  }
  
  private static interface vsendSMSMulti extends SMSGroup.vSMSGroupFull, SMSGroup.vSMSGroupSMSListOnly {}
}
