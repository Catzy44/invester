package me.catzy.prestiz.objects.wypelnioneParagrafy;

import jakarta.transaction.Transactional;
import me.catzy.prestiz.objects.grupy.GrupyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WypelnioneParagrafyService {
  @Autowired
  WypelnioneParagrafyRepository repo;
  
  @Autowired
  GrupyService serviceGrupy;
  
  public WypelnionyParagraf byId(int id) {
    return this.repo.getById(id);
  }
  
  public WypelnionyParagraf save(WypelnionyParagraf paragraf) {
    return (WypelnionyParagraf)this.repo.saveAndFlush(paragraf);
  }
}
