package me.catzy.prestiz.objects.spolki;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpolkiService {
  @Autowired
  private SpolkiRepository repo;
  
  public List<Spolka> getByIdIn(int[] ids) {
    return this.repo.getByIdIn(ids);
  }
  
  public Spolka getById(int ids) {
    return this.repo.getById(ids);
  }
}
