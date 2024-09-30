package me.catzy.prestiz.objects.formularze;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.miasta.MiastaService;
import me.catzy.prestiz.objects.miejsca.MiejscaService;
import me.catzy.prestiz.objects.paragrafy.Paragraf;
import me.catzy.prestiz.objects.paragrafy.ParagrafyService;
import me.catzy.prestiz.objects.sms.SMSService;
import me.catzy.prestiz.objects.sms_codes.SMSCode;
import me.catzy.prestiz.objects.sms_codes.SMSCodeService;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.uczestnicy_dane.UczestnicyDaneService;
import me.catzy.prestiz.objects.uczestnicy_dane.UczestnikDane;
import me.catzy.prestiz.objects.umowy.Umowa;
import me.catzy.prestiz.objects.umowy.UmowyService;
import me.catzy.prestiz.objects.wypelnioneParagrafy.FilledParagraphData;
import me.catzy.prestiz.objects.wypelnioneParagrafy.WypelnioneParagrafyService;
import me.catzy.prestiz.objects.wypelnioneParagrafy.WypelnionyParagraf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FormularzeService {
  @Autowired
  private FormularzeRepository repo;
  
  @Autowired
  private ParagrafyService serviceParagrafy;
  
  @Autowired
  UmowyService serviceUmowy;
  
  @Autowired
  WypelnioneParagrafyService serviceWypelnioneParagrafy;
  
  @Autowired
  SMSService serviceSMS;
  
  @Autowired
  SMSCodeService serviceSMSCode;
  
  @Autowired
  MiejscaService serviceMiejsca;
  
  @Autowired
  MiastaService serviceMiasta;
  
  @Autowired
  UczestnicyService serviceUczestnicy;
  
  @Autowired
  UczestnicyDaneService serviceUczestnicyDane;
  
  SimpleDateFormat d = new SimpleDateFormat("MM-dd-yy");
  
  public Formularz byIdLazy(int id) {
    return this.repo.getOne(id);
  }
  
  public List<Formularz> getAllActive() {
    return this.repo.getAllActive();
  }
  
  @Cacheable(value = {"formularze"}, key = "#id", condition = "#id != 0")
  public Formularz getById(int id) {
    return this.repo.getById(id);
  }
  
  public List<Paragraf> getParagraphListWithAdditionalData(Formularz form) {
    List<Paragraf> paragrafy = form.getParagrafy();
    for (Paragraf p : paragrafy)
      this.serviceParagrafy.attachAdditionalData(p); 
    return paragrafy;
  }
  
  public Map<String, Object> buildActiveFormularz() {
    return null;
  }
  
  public Umowa submitForm(WypelnionyParagraf[] paragrafy) throws Exception {
    Umowa umowa = new Umowa();
    umowa = this.serviceUmowy.save(umowa);
    int smsCode = -1;
    List<String> phone_numbers = new ArrayList<>();
    Uczestnik ucz = new Uczestnik();
    ucz.setTimestamp(new Timestamp(System.currentTimeMillis()));
    ucz.setPlace(paragrafy[0].getParagraf().getMiejsce());
    ucz.setDateAdded(LocalDate.now());
    for (int i = 0; i < paragrafy.length; i++) {
      WypelnionyParagraf p = paragrafy[i];
      FilledParagraphData data = p.getData();
      p.setUmowa(umowa);
      if (p.getType() == 8) {
        smsCode = p.getData().getCode();
      } else if (p.getType() == 2) {
        UczestnikDane ud = new UczestnikDane(data);
        ud = this.serviceUczestnicyDane.save(ud);
        phone_numbers.add(p.getData().getTelefon());
      } else if (p.getType() == 3) {
        UczestnikDane ud = new UczestnikDane(data);
        ud = this.serviceUczestnicyDane.save(ud);
        phone_numbers.add(p.getData().getTelefon());
      } 
    } 
    if (smsCode == -1)
      throw new UserException("sms code not found", "nie podałeś kodu sms"); 
    if (phone_numbers.size() == 0)
      throw new UserException("phone number not found", "nie podałeś numeru telefonu"); 
    SMSCode code = this.serviceSMSCode.checkSMSCode(smsCode, phone_numbers);
    code.setActive(false);
    code = this.serviceSMSCode.save(code);
    for (int j = 0; j < paragrafy.length; j++) {
      WypelnionyParagraf p2 = paragrafy[j];
      p2 = this.serviceWypelnioneParagrafy.save(p2);
    } 
    ucz = this.serviceUczestnicy.save(ucz);
    umowa.setUczestnik(ucz);
    umowa.setCreated(new Timestamp(System.currentTimeMillis()));
    umowa = this.serviceUmowy.save(umowa);
    return umowa;
  }
}
