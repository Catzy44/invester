package me.catzy.prestiz.objects.sms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import me.catzy.prestiz.PrestizApplication;
import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.sms_group.SMSGroup;
import me.catzy.prestiz.objects.spolki.Spolka;
import me.catzy.prestiz.objects.uczestnicy.PhoneNumber;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.uczestnicy.personalData.AttenderPersonalInfo;

@Transactional
@Service
public class SMSService {
  @Autowired
  private SMSRepository repo;
  
  @Autowired
  private UczestnicyService serviceUcz;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  private long lastAndroidSMSGateActivityTS = -1L;
  
  SMSCache cache = null;
  
  public void updateStatusMultiple(int[] ids, int newStatus) {
    this.repo.updateStatusByIds(ids, newStatus);
  }
  
  public List<SMS> patchMultiple(List<Map<Object, Object>> list) throws UserException, JsonMappingException {
    List<SMS> smsList = new ArrayList<>();
    for (Map<Object, Object> map : list)
      smsList.add(patch(map)); 
    return smsList;
  }
  
  public SMS patch(Map<Object, Object> map) throws UserException, JsonMappingException {
    int id = -1;
    for (Object k_ : map.keySet()) {
      String k = (String)k_;
      if (k.equals("id")) {
        id = ((Integer)map.get(k)).intValue();
        break;
      } 
    } 
    if (id == -1) {
    	for(Object s : map.keySet()) {
    		String ss = (String)s;
    		System.out.println(ss+": "+map.get(s));
    	}
      throw new UserException("id key not found within object", "meow!"); 
    }
    SMS obj = this.repo.getById(id);
    SMS oldCpy = obj.clone();
    this.objectMapper.updateValue(obj, map);
    this.cache.messageChangedEvent(oldCpy, obj);
    return save(obj, false);
  }
  
  public SMS save(SMS s) {
    return save(s, true);
  }
  
  public SMS save(SMS s, boolean event) {
    PhoneNumber pn = new PhoneNumber(s.getNumber());
    if (pn.isValid())
      s.setNumber(pn.getNumberFixed()); 
    if (s.getCreatedTimestamp() == null)
      s.setCreatedTimestamp(new Timestamp(System.currentTimeMillis())); 
    if (event)
      this.cache.messageChangedEvent((SMS)null, s); 
    return (SMS)this.repo.save(s);
  }
  
  public SMS byId(int id) {
    return this.repo.getById(id);
  }
  
  public long getSecondsPassedTillLastSMSGateAct() {
    if (this.lastAndroidSMSGateActivityTS == -1L)
      return -1L; 
    return (System.currentTimeMillis() - this.lastAndroidSMSGateActivityTS) / 1000L;
  }
  
  public List<SMS> getAllToSend() {
    this.lastAndroidSMSGateActivityTS = System.currentTimeMillis();
    return this.repo.getAllToSend();
  }
  
  public SMS sendSMS(Uczestnik ucz, String content) throws Exception {
    return sendSMS(ucz, content, null);
  }
  
  public SMS sendSMS(Uczestnik ucz, String content, SMSGroup group) throws Exception {
    PhoneNumber pn = getPhoneNumberByUczestnik(ucz);
    if (!pn.isValid())
      throw new Exception("uczestnik has invalid phone number"); 
    SMS sms = new SMS();
    sms.setNumber(pn.getNumberFixed());
    sms.setUczestnik(ucz);
    sms.setContent(content);
    sms.setStatus(0);
    sms.setType(0);
    if (group != null)
      sms.setSMSGroup(group); 
    return save(sms);
  }
  
  public PhoneNumber getPhoneNumberByUczestnik(Uczestnik u) {
    PhoneNumber pn = new PhoneNumber(u.getTelefonOpiekuna());
    return pn;
  }
  
  @JsonView({SMSController.getSMSNewsWithUsersSearch.class})
  @Data
  static class FoundConversation {
    private Uczestnik attender;
    private AttenderPersonalInfo personalInfo;
    private Miejsce place;
    private Long unreadSMSCount;
    private AttenderPersonalInfo parentInfo;
    
    public FoundConversation(Uczestnik attender, Miejsce place, AttenderPersonalInfo personalInfo, AttenderPersonalInfo parentInfo, Long unreadSMSCount) {
      this.attender = attender;
      this.unreadSMSCount = unreadSMSCount;
      this.place = place;
      this.personalInfo = personalInfo;
      this.parentInfo = parentInfo;
    }
  }
  
  public List<FoundConversation> findConversationsByStr(String str) {
    return this.repo.findAttendersByStrInclUnreadSMSCount(str, (Pageable)PageRequest.of(0, 20));
  }
  
  public int getUnreadSMSCount() {
    return this.repo.getUnreadSMSCount();
  }
  
  public void markAllUczestnikMessagesAsRead(Uczestnik u) {
    this.cache.markAllRead(u.getTelefonOpiekuna());
    this.repo.markAllUczestnikMessagesAsRead(u);
  }
  
  public void markAllNumberMessagesAsRead(String number) {
    this.cache.markAllRead(number);
    this.repo.markAllNumberMessagesAsRead(number);
  }
  
  public List<SMS> getByNumber(String number) {
    return this.repo.getAllMessagesByNumber(number);
  }
  
  public int getUnreadCountByNumber(String number) {
    return this.repo.getUnreadCountByNumber(number);
  }
  
  public SMSCache getCache() {
    return this.cache;
  }

	@PostConstruct
	public void init() {
		this.cache = new SMSCache(this.serviceUcz, this.repo);
		
		if(!PrestizApplication.SMS_CACHE_PRELOAD_DISABLED) {
			System.out.println("PRELOADING CACHE...");
			this.cache.preloadCache();
		}
	}
  
  
  public String replaceAttenderSV(String s, Uczestnik u) {
    return s.replace("[UCZESTNIKIMIENAZWISKO]", u.getImie() + " " + u.getNazwisko());
  }
  
  public String replacePlaceSV(String s, Miejsce m) {
    return s.replace("[NAZWAMIEJSCA]", m.getName()).replace("[NRKONTASPOLKA]", m.getCompany().getAccNum());
  }
  
  public String replaceMoneySV(String s, float amount) {
    return s.replace("[ZKWOTA]", "" + amount);
  }
  
  public String replaceVariablesIfNecessary(String message, Uczestnik attender) {
	  if(message.contains("[UCZESTNIKIMIENAZWISKO]")) {
		  message = message.replace("[UCZESTNIKIMIENAZWISKO]", attender.getImie() + " " + attender.getNazwisko());
	  }
	  if(message.contains("[NAZWAMIEJSCA]")) {
		  Miejsce place = attender.getPlace();
		  message = message.replace("[NAZWAMIEJSCA]", place.getName());
	  }
	  if(message.contains("[NRKONTASPOLKA]")) {
		  Spolka company = attender.getPlace().getCompany();
		  message = message.replace("[NRKONTASPOLKA]", company.getAccNum());
	  }
	  if(message.contains("[ZKWOTA]")) {
		  float sumToPay = serviceUcz.getSumToPay(attender);
		  message = message.replace("[ZKWOTA]", sumToPay+"");
	  }
	  return message;
  }
  
  public static interface vSMSCEntry {}
}
