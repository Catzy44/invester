package me.catzy.prestiz.objects.seasons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Entity
@Table(name = "Lata")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonView({Season.vSeasonRest.class})
public class Season {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({vSeasonId.class})
  private int id;
  
  @JsonView({vSeasonName.class})
  @Column(name = "nazwa")
  private String label;
  
  private String timestamp;
  
  @Column(name = "od")
  private String startDate;
  
  @Column(name = "do")
  private String endDate;
  
  @JsonIgnore
  @OneToMany(mappedBy = "season")
  private List<Uczestnik> attenders;
  
  @JsonIgnore
  @OneToMany(mappedBy = "season")
  private List<Miejsce> places;
  
  @Transient
  @JsonIgnore
  public LocalDate getDateFrom() {
    return LocalDate.parse(this.startDate, formatter);
  }
  
  @Transient
  @JsonIgnore
  public LocalDate getDateTo() {
    return LocalDate.parse(this.endDate, formatter);
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public String getTimestamp() {
    return this.timestamp;
  }
  
  public String getStartDate() {
    return this.startDate;
  }
  
  public String getEndDate() {
    return this.endDate;
  }
  
  public List<Uczestnik> getAttenders() {
    return this.attenders;
  }
  
  public List<Miejsce> getPlaces() {
    return this.places;
  }
  
  @JsonView({vSeasonId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  @JsonView({vSeasonName.class})
  public void setLabel(String label) {
    this.label = label;
  }
  
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
  
  @JsonIgnore
  public void setAttenders(List<Uczestnik> attenders) {
    this.attenders = attenders;
  }
  
  @JsonIgnore
  public void setPlaces(List<Miejsce> places) {
    this.places = places;
  }
  
  @Transient
  @JsonIgnore
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);
  
  public static interface vSeasonRest {}
  
  public static interface vSeasonId {}
  
  public static interface vSeasonName {}
}
