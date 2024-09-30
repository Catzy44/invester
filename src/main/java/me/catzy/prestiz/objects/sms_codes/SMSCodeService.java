package me.catzy.prestiz.objects.sms_codes;

import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.sms.SMS;
import me.catzy.prestiz.objects.sms.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class SMSCodeService {
  @Autowired
  private SMSCodeRepository repo;
  
  @Autowired
  private SMSService smsService;
  
  public SMSCode byId(int id) {
    return this.repo.getById(id);
  }
  
  public SMSCode save(SMSCode s) {
    return (SMSCode)this.repo.save(s);
  }
  
  public SMSCode checkSMSCode(int code, List<String> phone) throws Exception {
    if (code == -1)
      throw new UserException("sms code not provided", "nie podałeś kodu sms"); 
    SMSCode sms_code = this.repo.findByCode(code);
    if (sms_code == null)
      throw new UserException("sms code not found", "podany kod sms nie pasuje"); 
    Optional<String> o = phone.stream().filter(ph -> ph.equals(sms_code.getNumber())).findAny();
    if (o.isEmpty())
      throw new UserException("sms code phone number mismatch", "podany kod sms nie pasuje do numeru"); 
    return sms_code;
  }
  
  public void sendSMSCode(String number) {
    int code = (new Random()).nextInt(900000) + 100000;
    SMS sms = new SMS();
    sms.setContent("#PRESTIŻ# Kod weryfikacyjny: " + code);
    sms.setNumber(number);
    sms.setType(0);
    sms.setStatus(0);
    sms.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
    SMSCode sms_code = new SMSCode();
    sms_code.setNumber(number);
    sms_code.setSms(sms);
    sms_code.setCode(code);
    sms_code.setUczestnik(null);
    sms_code.setCreated(sms.getCreatedTimestamp());
    sms = this.smsService.save(sms);
    sms_code = save(sms_code);
  }
}
