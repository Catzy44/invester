package me.catzy.prestiz.objects.miasta.closedMonths;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Entity
@Table(name = "MiesiaceRozliczeniowe")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonView({ClosedMonth.vClosedMonthRest.class})
public class ClosedMonth {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView({vClosedMonthId.class})
  private int id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idUczestnika")
  @JsonIgnore
  private Uczestnik attender;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idRoku")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Season season;
  
  @Column(name = "miesiac")
  private Integer month;
  
  @Column(name = "rok")
  private Integer year;
  
  @Column(name = "cykl")
  private Integer cycle;
  
  @Column(name = "kwota")
  private Float amount;
  
  public int getId() {
    return this.id;
  }
  
  public Uczestnik getAttender() {
    return this.attender;
  }
  
  public Season getSeason() {
    return this.season;
  }
  
  public Integer getMonth() {
    return this.month;
  }
  
  public Integer getYear() {
    return this.year;
  }
  
  public Integer getCycle() {
    return this.cycle;
  }
  
  public Float getAmount() {
    return this.amount;
  }
  
  @JsonView({vClosedMonthId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  @JsonIgnore
  public void setAttender(Uczestnik attender) {
    this.attender = attender;
  }
  
  public void setSeason(Season season) {
    this.season = season;
  }
  
  public void setMonth(Integer month) {
    this.month = month;
  }
  
  public void setYear(Integer year) {
    this.year = year;
  }
  
  public void setCycle(Integer cycle) {
    this.cycle = cycle;
  }
  
  public void setAmount(Float amount) {
    this.amount = amount;
  }
  
  public static interface vClosedMonthRest {}
  
  public static interface vClosedMonthId {}
  
  public static interface vClosedMonthFull extends vClosedMonthId, vClosedMonthRest {}
}
