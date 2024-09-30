package me.catzy.prestiz.objects.sms_codes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import me.catzy.prestiz.objects.sms.SMS;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "sms_codes")
public class SMSCode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "smsId")
  @JsonIgnore
  private SMS sms;
  
  private boolean active;
  
  private int code;
  
  private String number;
  
  @ManyToOne
  @JoinColumn(name = "uczestnikId", nullable = true)
  @JsonIgnore
  @Nullable
  @Basic(optional = true)
  private Uczestnik uczestnik = null;
  
  private Timestamp created;
  
  public int getId() {
    return this.id;
  }
  
  public SMS getSms() {
    return this.sms;
  }
  
  public boolean isActive() {
    return this.active;
  }
  
  public int getCode() {
    return this.code;
  }
  
  public String getNumber() {
    return this.number;
  }
  
  @Nullable
  public Uczestnik getUczestnik() {
    return this.uczestnik;
  }
  
  public Timestamp getCreated() {
    return this.created;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  @JsonIgnore
  public void setSms(SMS sms) {
    this.sms = sms;
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
  
  public void setCode(int code) {
    this.code = code;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  @JsonIgnore
  public void setUczestnik(@Nullable Uczestnik uczestnik) {
    this.uczestnik = uczestnik;
  }
  
  public void setCreated(Timestamp created) {
    this.created = created;
  }
}
