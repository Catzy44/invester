package me.catzy.prestiz.objects.grupy;

import java.time.temporal.ChronoField;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.miejsca.Miejsce.PlaceType;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.zajecia.ZajeciaRepository;

@Service
@Transactional
public class GrupyService {
  @Autowired
  private GrupyRepository repo;
  
  @Autowired
  private ZajeciaRepository repoZajecia;
  
  @Autowired
  private UczestnicyService serviceAttenders;
  
  @Cacheable(value = {"grupy"}, key = "#id", condition = "#id != 0")
  public Grupa byId(int id) {
    return this.repo.getById(id);
  }
  
  @Transactional
  public Grupa byIdWithAttenders(int id) {
    Grupa g = this.repo.getById(id);
    Hibernate.initialize(g.getUczestnicy());
    return g;
  }
  
  public Grupa[] getByIds(int[] ids) {
    if (ids.length == 0)
      return null; 
    return this.repo.getByIdIn(ids);
  }
  
  public Grupa byIdLazy(int id) {
    return (Grupa)this.repo.getReferenceById(Integer.valueOf(id));
  }
  
  public int countClassesInMonth(Grupa g, int monthNum) {
    return this.repoZajecia.countByGroupAndMonthNum(g, monthNum).intValue();
  }
  
  public Map<Integer, Float> getClassPricesForAllMonthsByGroupId(int id) {
    Grupa group = byId(id);
    Miejsce m = group.getPlace();
    Season s = group.getSeason();
    if (m.getType() == 0) {
      int monthNumFrom = s.getDateFrom().get(ChronoField.MONTH_OF_YEAR);
      Map<Integer, Float> map = new LinkedHashMap<>();
      float pricePerClass = group.getPriceForOneClass();
      for (int n = 0; n < 12; n++) {
        int i = (n + monthNumFrom - 1) % 12 + 1;
        int thisMonthClassesCount = countClassesInMonth(group, i);
        map.put(Integer.valueOf(i), Float.valueOf(pricePerClass * thisMonthClassesCount));
      } 
      return map;
    } 
    if (m.getType() == PlaceType.ADULTS) {
      Integer biggestCycleNum = this.repoZajecia.getBiggestCycleNum(group);
      
      //no classes in db for this group
      if(biggestCycleNum == null) {
    	  return null;
      }
      
      Map<Integer, Float> map = new LinkedHashMap<>();
      float pricePerClass = group.getPriceForOneClass();
      for (int n = 1; n < biggestCycleNum + 1; n++) {
        int thisCycleClassesCount = this.repoZajecia.countByGroupAndCycleNum(group, n).intValue();
        map.put(Integer.valueOf(n), Float.valueOf(pricePerClass * thisCycleClassesCount));
      } 
      return map;
    } 
    return null;
  }
  
  public List<Uczestnik> getAttendersByGroupId(int gri) {
    Grupa gr = byId(gri);
    Hibernate.initialize(gr.getUczestnicy());
    return gr.getUczestnicy();
  }
  
  public List<Uczestnik> getDischargedAttendersByPlace(Miejsce m) {
    List<Uczestnik> att = this.repo.getDischargedAttenders(m);
    return att;
  }
}
