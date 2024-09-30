package me.catzy.prestiz.objects.miasta.closedMonths;

import java.util.List;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/closedMonths"})
public class ClosedMonthController {
  @Autowired
  ClosedMonthService service;
  
  @Autowired
  UczestnicyService serviceAttenders;
  
  @GetMapping
  public List<ClosedMonth> getAllCities() {
    return this.service.getAll();
  }
  
  @PostMapping
  public ClosedMonth set(@RequestBody ClosedMonthD cmd) {
    Uczestnik u = this.serviceAttenders.byIdLazy(cmd.attenderId);
    ClosedMonth cm = this.service.findByAttenderAndMonth(u, cmd.month);
    if (cm == null) {
      cm = new ClosedMonth();
      cm.setAttender(u);
      if (cmd.month != -1) {
        cm.setMonth(Integer.valueOf(cmd.month));
      } else {
        cm.setCycle(Integer.valueOf(cmd.cycle));
      } 
    } 
    cm.setAmount(cmd.amount);
    return this.service.save(cm);
  }
  
  @DeleteMapping({"/{id}"})
  public void delete(@PathVariable("id") int id) {
    this.service.delete(this.service.byIdLazy(id));
  }
  
  static class ClosedMonthD {
    int attenderId;
    
    int month = -1;
    
    int cycle = -1;
    
    float amount;
    
    public int getAttenderId() {
      return this.attenderId;
    }
    
    public int getMonth() {
      return this.month;
    }
    
    public int getCycle() {
      return this.cycle;
    }
    
    public float getAmount() {
      return this.amount;
    }
    
    public void setAttenderId(int attenderId) {
      this.attenderId = attenderId;
    }
    
    public void setMonth(int month) {
      this.month = month;
    }
    
    public void setCycle(int cycle) {
      this.cycle = cycle;
    }
    
    public void setAmount(float amount) {
      this.amount = amount;
    }
    
    public boolean equals(Object o) {
      if (o == this)
        return true; 
      if (!(o instanceof ClosedMonthD))
        return false; 
      ClosedMonthD other = (ClosedMonthD)o;
      return (other.canEqual(this) && getAttenderId() == other.getAttenderId() && getMonth() == other.getMonth() && getCycle() == other.getCycle() && Float.compare(getAmount(), other.getAmount()) == 0);
    }
    
    protected boolean canEqual(Object other) {
      return other instanceof ClosedMonthD;
    }
    
    public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + getAttenderId();
      result = result * 59 + getMonth();
      result = result * 59 + getCycle();
      result = result * 59 + Float.floatToIntBits(getAmount());
      return result;
    }
    
    public String toString() {
      return "ClosedMonthController.ClosedMonthD(attenderId=" + getAttenderId() + ", month=" + getMonth() + ", cycle=" + getCycle() + ", amount=" + getAmount();
    }
  }
}
