package me.catzy.prestiz.objects.miejsca;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.formularze.Formularz;
import me.catzy.prestiz.objects.formularze.FormularzeRepository;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.sms.SMSService;
import me.catzy.prestiz.objects.sms_group.SMSGroup;
import me.catzy.prestiz.objects.sms_group.SMSGroupService;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Service
public class MiejscaService {
  @Autowired
  private MiejscaRepository repo;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private FormularzeRepository repoFormularze;
  
  @Autowired
  private UczestnicyService serviceAttenders;
  
  @Autowired
  private SMSService serviceSMS;
  
  @Autowired
  private SMSGroupService serviceSMSGr;
  
  public List<Miejsce> searchByName(String name) {
    return this.repo.searchByName(name, 10);
  }
  
  public Miejsce byId(int id) {
    return this.repo.getById(id);
  }
  
  public Formularz getAktywnyFormularz(Miejsce m) {
    return this.repoFormularze.getAktywnyFormularz(m.getId());
  }
  
  public List<Miejsce> getByIdIn(int[] l) {
    return this.repo.getByIdIn(l);
  }
  
  public List<Miejsce> findByName(String name, Season season) {
    if (name.length() < 1)
      return null; 
    return this.repo.findMiejsceByName(name, season, (Pageable)PageRequest.of(0, 10));
  }
  
  public Miejsce byIdLazy(int id) {
    return this.repo.getOne(id);
  }
  
  public List<Miejsce> getAll() {
    return this.repo.getAll();
  }
  
  public float sumAllPayments(Miejsce m) {
    return (float)m.getPayments().stream().mapToDouble(e -> Double.valueOf(e.getAmount()).doubleValue()).sum();
  }
  
  public Map<Integer, UczestnicyService.PaymentStatus> getPaymentStatuses(Miejsce m) {
    Map<Integer, Float> costs = getClassesCosts(m);
    float sumOfAllPayments = sumAllPayments(m);
    Map<Integer, UczestnicyService.PaymentStatus> statuses = new LinkedHashMap<>();
    for (Map.Entry<Integer, Float> entry : costs.entrySet()) {
      int monthNum = ((Integer)entry.getKey()).intValue();
      float priceForThisMonth = ((Float)entry.getValue()).floatValue();
      float amountPaidForThisClass = 0.0F;
      if (priceForThisMonth <= sumOfAllPayments) {
        amountPaidForThisClass = priceForThisMonth;
      } else if (sumOfAllPayments > 0.0F) {
        amountPaidForThisClass = sumOfAllPayments;
      } 
      if (amountPaidForThisClass == priceForThisMonth) {
        statuses.put(monthNum, new UczestnicyService.PaymentStatus(0, amountPaidForThisClass, priceForThisMonth));
      } else if (amountPaidForThisClass > 0.0F) {
        statuses.put(monthNum, new UczestnicyService.PaymentStatus(1, amountPaidForThisClass, priceForThisMonth));
      } else {
        statuses.put(monthNum, new UczestnicyService.PaymentStatus(2, amountPaidForThisClass, priceForThisMonth));
      } 
      sumOfAllPayments -= priceForThisMonth;
    } 
    for (int i = 1; i < 13; i++) {
      if (!statuses.containsKey(i)) {
        statuses.put(i, new UczestnicyService.PaymentStatus(3));
      }
    } 
    return statuses;
  }
  
  public Map<Integer, Float> getClassesCosts(Miejsce m) {
    Map<Integer, Float> construct = new HashMap<>();
    List<Grupa> groups = m.getGroups();
    for (Grupa g : groups) {
      List<Uczestnik> attenders = g.getUczestnicy();
      for (Uczestnik attender : attenders) {
        Map<Integer, Float> thisAttenderPrices = serviceAttenders.sumPricesForClasses(attender);
        for (Iterator<Integer> iterator = thisAttenderPrices.keySet().iterator(); iterator.hasNext(); ) {
          int monthNum = iterator.next();
          if (!construct.containsKey(monthNum)) {
            construct.put(monthNum, 0.0F);
          }
          construct.put(monthNum, construct.get(monthNum) + thisAttenderPrices.get(monthNum));
        } 
      } 
    } 
    return construct;
  }
  
	public Miejsce patch(int id, Map<Object, Object> map) throws UserException, JsonMappingException {
		Miejsce obj = this.repo.getById(id);
		this.objectMapper.updateValue(obj, map);
		return save(obj);
	}

	public Miejsce save(Miejsce s) {
		return (Miejsce) this.repo.save(s);
	}

  public void sendSMSToThoseWhoNotPaid(Miejsce m, String sms) {
    List<Uczestnik> attenders = m.getUczestnicy();
    if (m.getType() == 1)
      return; 
    
    sms = this.serviceSMS.replacePlaceSV(sms, m);
    
    SMSGroup gr = new SMSGroup();
    gr.setPlace(m);
    gr.setTimestamp(new Timestamp(System.currentTimeMillis()));
    this.serviceSMSGr.save(gr);
    for (Uczestnik u : attenders) {
      float stp = this.serviceAttenders.getSumToPay(u);
      if (stp > 0.0F) {
    	  
        String smsU = this.serviceSMS.replaceAttenderSV(this.serviceSMS.replaceMoneySV(sms, stp), u);
        
        try {
          this.serviceSMS.sendSMS(u, smsU, gr);
        } catch (Exception e) {
          e.printStackTrace();
        } 
      } 
    } 
  }
}
