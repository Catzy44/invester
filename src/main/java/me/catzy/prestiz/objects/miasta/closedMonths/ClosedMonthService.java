package me.catzy.prestiz.objects.miasta.closedMonths;

import java.util.List;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClosedMonthService {
  @Autowired
  private ClosedMonthRepository repo;
  
  public List<ClosedMonth> getByIdIn(int[] ids) {
    return this.repo.getByIdIn(ids);
  }
  
  public List<ClosedMonth> getAll() {
    return this.repo.findAll();
  }
  
  public ClosedMonth save(ClosedMonth s) {
    return (ClosedMonth)this.repo.save(s);
  }
  
  public ClosedMonth byIdLazy(int id) {
    return (ClosedMonth)this.repo.getOne(Integer.valueOf(id));
  }
  
  public void delete(ClosedMonth ent) {
    this.repo.delete(ent);
  }
  
  public ClosedMonth findByAttenderAndMonth(Uczestnik u, int month) {
    return this.repo.findByAttenderAndMonth(u, month);
  }
}
