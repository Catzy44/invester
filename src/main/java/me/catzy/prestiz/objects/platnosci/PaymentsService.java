package me.catzy.prestiz.objects.platnosci;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.Data;
import me.catzy.prestiz.objects.spolki.Spolka;
import me.catzy.prestiz.objects.spolki.SpolkiService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Service
@Transactional
public class PaymentsService {
  @Autowired
  PaymentsRepository repo;
  
  @Autowired
  CacheManager cacheManager;
  
  @Autowired
  SpolkiService serviceSpolki;
  
  public Payment byIdLazy(int id) {
    return this.repo.getOne(id);
  }
  
  public Payment byId(int id) {
    return this.repo.findById(id);
  }
  
  public Payment save(Payment ucz) {
    return (Payment)this.repo.saveAndFlush(ucz);
  }
  
  public List<Payment> getByIdsIn(int[] id) {
    return this.repo.getByIdIn(id);
  }
  
  public float getTotalMoneyPaidByAttender(Uczestnik u) {
    Float s = this.repo.getTotalMoneyPaidByAttender(u);
    return (s == null) ? 0.0F : s.floatValue();
  }
  
  public void loadPayments(JSONPayments p) {
    Spolka s = this.serviceSpolki.getById(p.getActiveCompanyId());
    if (!s.getAccNum().equals(p.getAccNum()));
  }
  
  @Data
  public static class JSONPayment {
    private String date;
    private String title;
    private String senderAndReceiver;
    private float amount;
    private String accNum;
  }
  
  @Data
  public static class JSONPayments {
    private List<PaymentsService.JSONPayment> przelewy;
    private int activeCompanyId;
    private String accNum;
  }
}
