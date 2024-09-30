package me.catzy.prestiz.objects.miejsca;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import me.catzy.prestiz.ToolBox;
import me.catzy.prestiz.objects.formularze.Formularz;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.grupy.GrupyService;
import me.catzy.prestiz.objects.seasons.SeasonsService;
import me.catzy.prestiz.objects.sms.SMS;
import me.catzy.prestiz.objects.sms_group.SMSGroup;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.uczestnicy.personalData.AttenderPersonalInfo;

@RestController
@RequestMapping({"/miejsca", "/places"})
public class MiejscaController {
  @Autowired
  MiejscaService service;
  
  @Autowired
  UczestnicyService serviceUczestnicy;
  
  @Autowired
  GrupyService serviceGroups;
  
  @Autowired
  SeasonsService serviceSeasons;
  
  @Autowired
  private ToolBox box;

  @PatchMapping({"{id}"})
  private Miejsce updateStatusMultipleByIds(@PathVariable("id") int id, @RequestBody Map<Object, Object> list) throws Exception {
    return service.patch(id,list);
  }
  
  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String projection) {
    return new ResponseEntity(this.box.projectList(this.service.getAll(), projection), (HttpStatusCode)HttpStatus.OK);
  }
  
  @JsonView({getMiejsceWithGroups.class})
  @GetMapping({"{id}/with_groups"})
  public Miejsce getMiejsceWithGroups(@PathVariable("id") int id) {
    return this.service.byId(id);
  }
  
  @GetMapping({"{id}"})
  public ResponseEntity getMiejsce(@PathVariable("id") int id, @RequestParam(required = false) String projection) {
    return new ResponseEntity(this.box.projectObj(this.service.byId(id), projection), (HttpStatusCode)HttpStatus.OK);
  }
  
  @GetMapping({"{id}/aktywny_formularz"})
  public Formularz aktywnyFormularz(@PathVariable("id") int id) {
    return this.service.getAktywnyFormularz(this.service.byId(id));
  }
  
  @GetMapping({"{id}/chetni_uczestnicy"})
  public List<Uczestnik> chetniUcczestnicy(@PathVariable("id") int id) {
    return this.serviceUczestnicy.getChetni(id);
  }
  
  @JsonView({getSmsGroups.class})
  @GetMapping({"{id}/sms_groups"})
  public List<SMSGroup> getSmsGroups(@PathVariable("id") int id) {
    return this.service.byId(id).getSmsGroups();
  }
  
  @GetMapping({"{id}/payments"})
  public Map<Integer, UczestnicyService.PaymentStatus> getPayments(@PathVariable("id") int id) {
    return this.service.getPaymentStatuses(this.service.byId(id));
  }
  
  @JsonView({getDiscAttendders.class})
  @GetMapping({"{id}/attendersDischarged"})
  public List<Uczestnik> getDiscAttendders(@PathVariable("id") int id) {
    return this.serviceGroups.getDischargedAttendersByPlace(this.service.byId(id));
  }
  
  @GetMapping({"{id}/groups"})
  public List<Grupa> getGroups(@PathVariable("id") int id) {
    return this.service.byId(id).getGroups();
  }
  
  @Data
  static class JSONSMSContent {
    private String content;
  }
  
  @PostMapping({"{id}/payments/sendSMSesToOverdueAttenders"})
  public void getPayments(@PathVariable("id") int id, @RequestBody JSONSMSContent cont) {
    this.service.sendSMSToThoseWhoNotPaid(this.service.byId(id), cont.getContent());
  }
  
  @Data
  static class FindMiejsceDC {
    private String name;
    private int season;
  }
  
  @JsonView({findMiejsceByName.class})
  @PostMapping({"/search"})
  public List<Miejsce> findMiejsceByName(@RequestBody FindMiejsceDC data) {
    return this.service.findByName(data.getName(), this.serviceSeasons.getByIdLazy(data.getSeason()));
  }
  
  private static interface findMiejsceByName extends Miejsce.vMiejsceId, Miejsce.vMiejsceNazwa, Miejsce.vMiejsceAdres {}
  
  private static interface getDiscAttendders extends Uczestnik.vUczestnikId, Uczestnik.vUczestnikName, Uczestnik.vUczestnikGrupa, Uczestnik.vUczestnikRest, Uczestnik.vUczestnikPersonalInfo, AttenderPersonalInfo.vAttenderPersonalInfoFull, Grupa.vGrupaId, Grupa.vGrupaNazwa {}
  
  private static interface getSmsGroups extends SMS.vSMSAttenders, SMSGroup.vSMSGroupFull, SMSGroup.vSMSGroupSMSListOnly, SMS.vSMSRest, SMS.vSMSUczestnik, Uczestnik.vUczestnikId, Uczestnik.vUczestnikName, Uczestnik.vUczestnikGrupa {}
  
  private static interface getMiejsceWithGroups extends Miejsce.vMiejsceGrupy, Miejsce.vMiejsceId, Miejsce.vMiejsceNazwa, Miejsce.vMiejsceRest, Grupa.vGrupaId, Grupa.vGrupaNazwa {}
}
