package me.catzy.prestiz.objects.sms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import me.catzy.prestiz.objects.gcaptcha.GCaptchaService;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.sms_codes.SMSCodeService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.uczestnicy.personalData.AttenderPersonalInfo;

@RestController
@RequestMapping({"/sms"})
public class SMSController {
  @Autowired
  private SMSService service;
  
  @Autowired
  private SMSCodeService serviceCodes;
  
  @Autowired
  private GCaptchaService serviceGCaptcha;
  
  @PatchMapping({"multi"})
  private List<SMS> updateStatusMultipleByIds(@RequestBody List<Map<Object, Object>> list) throws Exception {
    return this.service.patchMultiple(list);
  }
  
  @PatchMapping
  private SMS updateStatusMultipleByIds(@RequestBody Map<Object, Object> list) throws Exception {
    return this.service.patch(list);
  }
  
  @PostMapping({"multi"})
  public List<SMS> uploadMultipleSMSes(@RequestBody List<SMS> smsesToSend) {
    for (SMS sms : smsesToSend)
      sms = this.service.save(sms); 
    return smsesToSend;
  }
  
  @JsonView({getSMSDoWyslania.class})
  @GetMapping({"/toSend"})
  public List<SMS> getSMSDoWyslania() {
    return this.service.getAllToSend();
  }
  
  @GetMapping({"/toSend/seconds_passed_till_last"})
  public long getSecondsPassedTillLast() {
    return this.service.getSecondsPassedTillLastSMSGateAct();
  }
  
  @PostMapping({"p_send_confirmation_sms"})
  public void sendPublicSmsConf(@RequestBody ConfirmationSMSRequest req, HttpServletRequest request) throws Exception {
    this.serviceGCaptcha.checkCaptcha(req.getCaptcha(), request.getRemoteAddr());
    this.serviceCodes.sendSMSCode(req.getPhone());
  }
  
  @PostMapping
  public SMS receivedSMSasdf(@RequestBody SMS sms) {
    return this.service.postSMS(sms);
  }
  
  @GetMapping({"/unreadSMSCount"})
  public int getUnreadSMSCount() {
    return this.service.getUnreadSMSCount();
  }
  
  @GetMapping({"/chid"})
  public int getLatestSMSId() {
    return this.service.getCache().getChangeItterator();
  }
  
  @Data
  static class FindConversationDataClass {
    String str;
  }
  @JsonView({getSMSNewsWithUsersSearch.class})
  @PostMapping({"/news/find"})
  public List<SMSService.FoundConversation> findConversationsByStr(@RequestBody FindConversationDataClass data) {
    return this.service.findConversationsByStr(data.getStr());
  }
  
  @JsonView({vgetSMSNewsWithUsers.class})
  @GetMapping({"/news/{idx}"})
  public List<SMSService.SMSCache.SMSCEntry> getSMSNewsPartWithUsers(@PathVariable("idx") int idx) {
    return this.service.getCache().getCachedSMSNewsPart(idx, 1);
  }
  
  @Data
  static class NewsQuestionData {
    private int index;
    private int count;
  }
  @JsonView({vgetSMSNewsWithUsers.class})
  @PostMapping({"/news"})
  public List<SMSService.SMSCache.SMSCEntry> getSMSNewsPartWithUsers(@RequestBody NewsQuestionData data) {
    return this.service.getCache().getCachedSMSNewsPart(data.getIndex(), data.getCount());
  }
  
  @Data
  static class PhoneNumberData {
    private String number;
  }
  @JsonView({SMS.vSMSFull.class})
  @PostMapping({"/by_number"})
  public List<SMS> getSMSesByNumber(@RequestBody PhoneNumberData phone) {
    return this.service.getByNumber(phone.getNumber());
  }
  
  @PostMapping({"/by_number/unread_count"})
  public int getSMSUnreadCountByNumber(@RequestBody PhoneNumberData phone) {
    return this.service.getUnreadCountByNumber(phone.getNumber());
  }
  
  @PostMapping({"/by_number/mark_all_read"})
  public void markAllNumberMessagesAsRead(@RequestBody PhoneNumberData phone) {
    this.service.markAllNumberMessagesAsRead(phone.getNumber());
  }
  
  @JsonView({SMS.vSMSFull.class})
  @PostMapping({"/by_number/chid"})
  public int getLatestSMS(@RequestBody PhoneNumberData phone) {
    SMSService.SMSCache.SMSCEntry e = this.service.getCache().getByNumber(phone.getNumber());
    if (e == null)
      return 0; 
    return e.getChangeItterator();
  }

  
  private static interface getSMSDoWyslania extends SMS.vSMSRest {}
  
  private static interface vgetSMSNewsWithUsers extends SMSService.vSMSCEntry, SMS.vSMSRest, SMS.vSMSUczestnik, SMS.vSMSReadStatus, SMS.vSMSAttenders, Uczestnik.vUczestnikName, Uczestnik.vUczestnikId, Uczestnik.vUczestnikMiejsce, Uczestnik.vUczestnikSeason, Miejsce.vMiejsceId, Miejsce.vMiejsceNazwa, Season.vSeasonId, Season.vSeasonName {}
  
  public static interface getSMSNewsWithUsersSearch extends SMS.vSMSRest, SMS.vSMSUczestnik, Uczestnik.vUczestnikId, AttenderPersonalInfo.vAttenderPersonalInfoFull, Miejsce.vMiejsceId, Miejsce.vMiejsceNazwa {}
}
