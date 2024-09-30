package me.catzy.prestiz.objects.grupy;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import java.util.Map;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.uczestnicy.personalData.AttenderPersonalInfo;
import me.catzy.prestiz.objects.zajecia.ZajeciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/grupy"})
public class GrupyController {
  @Autowired
  GrupyService service;
  
  @Autowired
  ZajeciaRepository repoZajecia;
  
  @JsonView({getGrupaWithGroups.class})
  @GetMapping({"{id}/with_attenders"})
  public Grupa getGrupaWithGroups(@PathVariable("id") int id) {
    return this.service.byIdWithAttenders(id);
  }
  
  @GetMapping({"{id}/classPrices"})
  public Map<Integer, Float> getClassPricesForAllMonths(@PathVariable("id") int id) {
    return this.service.getClassPricesForAllMonthsByGroupId(id);
  }
  
  @JsonView({getAttendersList.class})
  @GetMapping({"{id}/attenders"})
  public List<Uczestnik> getAttendersList(@PathVariable("id") int id) {
    return this.service.getAttendersByGroupId(id);
  }
  
  @GetMapping({"{id}/biggestCycleNum"})
  public int getBiggestCycleNum(@PathVariable("id") int id) throws Exception {
    Grupa gr = this.service.byId(id);
    if (gr == null)
      throw new Exception("gr nf"); 
    return this.repoZajecia.getBiggestCycleNum(gr).intValue();
  }
  
  private static interface getGrupaWithGroups extends Grupa.vGrupaId, Grupa.vGrupaNazwa, Grupa.vGrupaUczestnikList, Grupa.vGrupaGodziny, Uczestnik.vUczestnikId, Uczestnik.vUczestnikName, Uczestnik.vUczestnikGrupa, Uczestnik.vUczestnikTel {}
  
  private static interface getAttendersList extends Uczestnik.vUczestnikFull, Uczestnik.vUczestnikPersonalInfo, AttenderPersonalInfo.vAttenderPersonalInfoFull {}
}
