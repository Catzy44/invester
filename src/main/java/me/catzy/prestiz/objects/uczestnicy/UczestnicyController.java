package me.catzy.prestiz.objects.uczestnicy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import me.catzy.prestiz.objects.attendersGroupChanges.AttenderGroupChange;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.grupy.GrupyService;
import me.catzy.prestiz.objects.miasta.closedMonths.ClosedMonth;
import me.catzy.prestiz.objects.miejsca.MiejscaService;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.sms.SMSService;
import me.catzy.prestiz.objects.umowy.Umowa;

@RestController
@Transactional
@RequestMapping({"/uczestnicy"})
public class UczestnicyController {
  @Autowired
  UczestnicyService service;
  
  @Autowired
  SMSService serviceSMS;
  
  @Autowired
  MiejscaService serviceMiejsca;
  
  @Autowired
  GrupyService serviceGrupy;
  
  @GetMapping({"/{id}/umowy"})
  public List<Umowa> searchBydghfName(@PathVariable int id) {
    return this.service.byId(id).getUmowy();
  }
  
  @JsonView({getUczestnik.class})
  @GetMapping({"/{id}"})
  public Uczestnik getUczestnik(@PathVariable int id) {
    return this.service.byId(id);
  }
  
  @JsonView({getUczestnikWithMiejsce.class})
  @GetMapping({"/{id}/with_miejsce"})
  public Uczestnik getUczestnikWithMiejsce(@PathVariable int id) {
    return this.service.byId(id);
  }
  
  @PostMapping({"/{id}/copy_to"})
  public Uczestnik copyUczestnik(@PathVariable("id") int id, @RequestBody CopyUczestnikData data) {
    return this.service.copyUczestnikTo(this.service.byId(id), this.serviceMiejsca.byId(data.getToPlaceId()), this.serviceGrupy.byId(data.getToGroupId()));
  }
  
  @GetMapping({"/{id}/payments"})
  public Map<Integer, UczestnicyService.PaymentStatus> getPayments(@PathVariable("id") int id) {
    return this.service.getPaymentStatuses(this.service.byId(id));
  }
  
  @GetMapping({"/{id}/payments/fresh"})
  public Map<Integer, UczestnicyService.PaymentStatus> getPaymentsFresh(@PathVariable("id") int id) {
    return this.service.getPaymentStatuses(this.service.byId(id), true);
  }
  
  @GetMapping({"/{id}/test"})
  public Object test(@PathVariable("id") int id) {
    return this.service.getClassMonths(this.service.byId(id));
  }
  
  @GetMapping({"/{id}/payments/sumToPay"})
  public float getSumToPay(@PathVariable("id") int id) {
    return this.service.getSumToPay(this.service.byId(id));
  }
  
  @JsonView({getClosedMonths.class})
  @GetMapping({"/{id}/closedMonths"})
  public List<ClosedMonth> getClosedMonths(@PathVariable("id") int id) {
    return this.service.byId(id).getClosedMonths();
  }
  
  @PostMapping({"/{id}/closedMonths"})
  public List<ClosedMonth> sClosedMonths(@PathVariable("id") int id, @RequestBody ClosedMonthSData data) {
    return this.service.byId(id).getClosedMonths();
  }
  
  @GetMapping({"/{id}/groupChanges"})
  public List<AttenderGroupChange> getGroupChanges(@PathVariable("id") int id) {
    return this.service.byId(id).getGroupChanges();
  }
  
  @GetMapping({"/{id}/group"})
  public Grupa getGroup(@PathVariable("id") int id) {
    return this.service.byId(id).getGroup();
  }
  
  @GetMapping({"/{id}/place"})
  public Miejsce getPlace(@PathVariable("id") int id) {
    return this.service.byId(id).getPlace();
  }
  
  @GetMapping({"/{id}/testa"})
  public float testtesta(@PathVariable("id") int id) {
    return this.service.getSumToPay(this.service.byId(id));
  }
  
  @JsonView({findByNameTel.class})
  @PostMapping({"/find"})
  public List<Uczestnik> findByNameTel(@RequestBody FindUczestnikData data) {
    return this.service.findByString(data.getStr());
  }
  
  @Data
  private static class CopyUczestnikData {
    private int toPlaceId;
    private int toGroupId;
  }
  
  @Data
  private static class ClosedMonthSData {
    private int month;
    private float amount;
  }
  
  @Data
  private static class FindUczestnikData {
    private String str;
  }
  
  @Data
  static class ShitDC {
    private String q;
  }
  
  private static interface getUczestnik extends Uczestnik.vUczestnikFull {}
  
  private static interface getUczestnikWithMiejsce extends Uczestnik.vUczestnikFull, Uczestnik.vUczestnikMiejsce, Uczestnik.vUczestnikSeason, Uczestnik.vUczestnikTel, Miejsce.vMiejsceId, Miejsce.vMiejsceNazwa, Season.vSeasonId, Season.vSeasonName {}
  
  private static interface getClosedMonths extends Uczestnik.vUczestnikFull, Uczestnik.vUczestnikClosedMonths, ClosedMonth.vClosedMonthFull {}
  
  private static interface findByNameTel extends Uczestnik.vUczestnikMiejsce, Uczestnik.vUczestnikGrupa, Uczestnik.vUczestnikId, Uczestnik.vUczestnikName, Uczestnik.vUczestnikTel, Miejsce.vMiejsceId, Miejsce.vMiejsceNazwa, Grupa.vGrupaId, Grupa.vGrupaNazwa {}
}
