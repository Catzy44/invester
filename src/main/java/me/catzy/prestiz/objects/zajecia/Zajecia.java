package me.catzy.prestiz.objects.zajecia;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.zajecia.presences.Presence;

@Entity
@Table(name = "Zajecia")
@JsonView({Zajecia.vZajeciaRest.class})
@Data
public class Zajecia {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({vZajeciaId.class})
  private int id;
  
  private String timestamp;
  
  private int idRoku;
  
  @ManyToOne
  @JoinColumn(name = "idGrupy")
  @JsonIgnore
  private Grupa group;
  
  private String oryginalnaData;
  
  @JsonView({vZajeciaData.class})
  @Column(name = "data")
  private LocalDate date;
  
  private String zmianaDatyInstruktor;
  
  private String zmianaDatyFirma;
  
  private int procentWynagrodzenia;
  
  private int zastepczyInstruktorId;
  
  @OneToMany(mappedBy = "classes")
  @JsonView({Zajecia.vZajeciaPresences.class})
  private List<Presence> presences;
  
  private Integer cykl;
  
  public static interface vZajeciaRest {}
  
  public static interface vZajeciaId {}
  public static interface vZajeciaPresences {}
  
  public static interface vZajeciaData {}
}
