package me.catzy.prestiz.objects.spolki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import me.catzy.prestiz.objects.miasta.Miasto;
import me.catzy.prestiz.objects.seasons.Season;

@Entity
@Table(name = "Spolki")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonView({Miasto.vMiastoRest.class})
public class Spolka {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({vMiastoId.class})
  private int id;
  
  private String nazwa;
  
  private String timestamp;
  
  @ManyToOne
  @JoinColumn(name = "idRoku")
  private Season season;
  
  @Column(name = "nrKonta")
  private String accNum;
  
  public int getId() {
    return this.id;
  }
  
  public String getNazwa() {
    return this.nazwa;
  }
  
  public String getTimestamp() {
    return this.timestamp;
  }
  
  public Season getSeason() {
    return this.season;
  }
  
  public String getAccNum() {
    return this.accNum;
  }
  
  @JsonView({vMiastoId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  public void setNazwa(String nazwa) {
    this.nazwa = nazwa;
  }
  
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  
  public void setSeason(Season season) {
    this.season = season;
  }
  
  public void setAccNum(String accNum) {
    this.accNum = accNum;
  }
  
  public static interface vMiastoRest {}
  
  public static interface vMiastoId {}
  
  public static interface vMiastoName {}
}
