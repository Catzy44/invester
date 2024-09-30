package me.catzy.prestiz.objects.miasta;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiastaService {
  @Autowired
  private MiastaRepository repo;
  
  public List<Miasto> getByIdIn(int[] ids) {
    return this.repo.getByIdIn(ids);
  }
  
  public List<Miasto> getAll() {
    return this.repo.findAll();
  }
}
