package me.catzy.prestiz.objects.paragrafy;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;
import me.catzy.prestiz.exceptions.ExceptionHandler;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.grupy.GrupyService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ParagrafyService {
  @Autowired
  ParagrafyRepository repo;
  
  @Autowired
  GrupyService serviceGrupy;
  
  @Transactional
  public void orderAfter(Paragraf toOrder, Paragraf after) {
    if (after.getOrd() > toOrder.getOrd()) {
      this.repo.moveForwards(toOrder.getOrd(), after.getOrd(), toOrder.getFormularz().getId());
      toOrder.setOrd(after.getOrd());
    } else {
      this.repo.moveBackwards(toOrder.getOrd(), after.getOrd(), toOrder.getFormularz().getId());
      toOrder.setOrd(after.getOrd() + 1);
    } 
    save(toOrder);
  }
  
  public Paragraf byIdLazy(int id) {
    return this.repo.getOne(id);
  }
  
  public Paragraf byId(int id) {
    return this.repo.getById(id);
  }
  
  public Paragraf save(Paragraf paragraf) {
    return (Paragraf)this.repo.saveAndFlush(paragraf);
  }
  
  public List<Paragraf> getByIdsIn(int[] id) {
    return this.repo.getByIdIn(id);
  }
  
  public void attachAdditionalData(Paragraf par) {
    if (par.getData() == null || par.getData() == null)
      return; 
    int type = par.getType();
    ParagrafData data = par.getData();
    try {
      if (type == 5) {
        if (data == null || data.getGroup() == null || data.getGroup().intValue() == 0)
          return; 
        Grupa grupa = this.serviceGrupy.byId(data.getGroup().intValue());
        Hibernate.initialize(grupa.getZajecia());
        data.setGroupData(grupa);
      } else if (type == 4 || type == 5 || type == 12) {
        if (data == null || data.getGroups() == null || (data.getGroups()).length == 0)
          return; 
        Grupa[] grs = (Grupa[])Stream.<Integer>of(data.getGroups()).map(grid -> {
              Grupa grupa = this.serviceGrupy.byId(grid.intValue());
              if (type == 12)
                Hibernate.initialize(grupa.getUczestnicy()); 
              return grupa;
            }).toArray(x -> new Grupa[x]);
        data.setGroupsData(grs);
      } 
    } catch (Exception e) {
      ExceptionHandler.logStacktrace(e);
    } 
  }
}
