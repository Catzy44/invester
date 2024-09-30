package me.catzy.prestiz.objects.umowy;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class UmowyService {
  @Autowired
  private UmowyRepository repo;
  
  public Umowa byId(int id) {
    return this.repo.getById(id);
  }
  
  public Umowa save(Umowa s) {
    return (Umowa)this.repo.save(s);
  }
}
