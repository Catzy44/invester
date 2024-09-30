package me.catzy.prestiz.objects.sms_group;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.sms.SMS;
import me.catzy.prestiz.objects.sms.SMSService;
import me.catzy.prestiz.objects.uczestnicy.PhoneNumber;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Transactional
@Service
public class SMSGroupService {
  @Autowired
  private SMSGroupRepository repo;
  
  @Autowired
  private SMSService serviceSMS;
  
  public SMSGroup byId(int id) {
    return this.repo.getById(id);
  }
  
  public SMSGroup save(SMSGroup s) {
    return (SMSGroup)this.repo.save(s);
  }
  
  public SMSGroup sendMultiSMS(List<Uczestnik> uczestnicy, String message, SMSGroupController.sendSMSMultiRB.Filters filters) throws UserException {
	  for (int i = 0; i < uczestnicy.size(); ++i) {
          final Uczestnik u = uczestnicy.get(i);
          final PhoneNumber pn = this.serviceSMS.getPhoneNumberByUczestnik(u);
          
          if ((int)uczestnicy.subList(0, i).stream().filter(attender->attender.getPhoneNumberParsed() != null && attender.getPhoneNumberParsed().equals(pn)).count() > 0) {
        	  continue;
        	  //SMSGroupService::lambda$sendMultiSMS$0
          }
          if (!pn.isValid()) {
              throw new UserException("user id " + u.getId() + " has invalid phone number", "Uczestnik " + u.getImie() + " " + u.getNazwisko() + " ma niepoprawny number telefonu");
          }
          u.setPhoneNumberParsed(pn);
      }
      final SMSGroup gr = new SMSGroup();
      gr.setPlace(uczestnicy.get(0).getPlace());
      gr.setTimestamp(new Timestamp(System.currentTimeMillis()));
      this.save(gr);
      for (final Uczestnik u2 : uczestnicy) {
          if (u2.getPhoneNumberParsed() == null) {
              continue;
          }
          if (filters != null) {
              if (filters.getAge() != null && filters.getAge().isEnabled()) {
                  final SMSGroupController.sendSMSMultiRB.Filters.FilterAge f = filters.getAge();
                  if (u2.getWiek() < f.getFrom()) {
                      continue;
                  }
                  if (u2.getWiek() > f.getTo()) {
                      continue;
                  }
              }
              if (filters.getSex() != null && filters.getSex().isEnabled()) {
                  final SMSGroupController.sendSMSMultiRB.Filters.FilterSex f2 = filters.getSex();
                  if (u2.getPlec() != f2.getValue()) {
                      continue;
                  }
              }
          }
          final SMS sms = new SMS();
          sms.setNumber(u2.getPhoneNumberParsed().getNumberFixed());
          sms.setUczestnik(u2);
          sms.setContent(serviceSMS.replaceVariablesIfNecessary(message, u2));
          sms.setStatus(0);
          sms.setType(0);
          sms.setSMSGroup(gr);
          this.serviceSMS.save(sms);
      }
      return gr;
  }
}
