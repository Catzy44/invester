package me.catzy.prestiz.objects.sms_group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.sms.SMS;

@Entity
@Table(name = "sms_group")
@JsonView({SMSGroup.vSMSGroupFull.class})
public class SMSGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @OneToMany(mappedBy = "SMSGroup", fetch = FetchType.EAGER)
  @JsonView({vSMSGroupSMSListOnly.class})
  private List<SMS> sms;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_miejsce")
  @JsonIgnore
  private Miejsce place;
  
  private Timestamp timestamp;
  
  public int getId() {
    return this.id;
  }
  
  public List<SMS> getSms() {
    return this.sms;
  }
  
  public Miejsce getPlace() {
    return this.place;
  }
  
  public Timestamp getTimestamp() {
    return this.timestamp;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  @JsonView({vSMSGroupSMSListOnly.class})
  public void setSms(List<SMS> sms) {
    this.sms = sms;
  }
  
  @JsonIgnore
  public void setPlace(Miejsce place) {
    this.place = place;
  }
  
  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
  
  public static interface vSMSGroupFull {}
  
  public static interface vSMSGroupSMSListOnly {}
}
