package me.catzy.prestiz.objects.sms_numbers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sms_numbers")
public class SMSNumber {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String number;
  
  private boolean pinned;
  
  public String getNumber() {
    return this.number;
  }
  
  public boolean isPinned() {
    return this.pinned;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public void setPinned(boolean pinned) {
    this.pinned = pinned;
  }
}
