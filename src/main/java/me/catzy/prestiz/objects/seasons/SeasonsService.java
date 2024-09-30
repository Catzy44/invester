package me.catzy.prestiz.objects.seasons;

import java.util.List;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeasonsService {
  @Autowired
  private SeasonsRepository repo;
  
  public List<Season> getByIdIn(int[] ids) {
    return this.repo.getByIdIn(ids);
  }
  
  public Season getById(int ids) {
    return this.repo.getById(ids);
  }
  
  public List<Season> getAll() {
    return this.repo.getAll();
  }
  
  public Season getByIdLazy(int id) {
    return this.repo.getOne(id);
  }
  
  public List<Uczestnik> getAllAttenders(Season s) {
    return s.getAttenders();
  }
}
