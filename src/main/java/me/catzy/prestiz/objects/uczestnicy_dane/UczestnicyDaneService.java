package me.catzy.prestiz.objects.uczestnicy_dane;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UczestnicyDaneService {
  @Autowired
  UczestnicyDaneRepository repo;
  
  public UczestnikDane byIdLazy(int id) {
    return this.repo.getOne(id);
  }
  
  public UczestnikDane byId(int id) {
    return this.repo.findById(id);
  }
  
  public UczestnikDane save(UczestnikDane dane) {
    return (UczestnikDane)this.repo.saveAndFlush(dane);
  }
}
