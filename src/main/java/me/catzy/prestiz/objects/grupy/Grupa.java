package me.catzy.prestiz.objects.grupy;

import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.zajecia.Zajecia;

@Entity
@Table(name = "Grupy")
@JsonView({Grupa.vGrupaRest.class})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
public class Grupa {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({vGrupaId.class})
  private int id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idMiejsca")
  @JsonIgnore
  private Miejsce place;
  
  @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
  @JsonIgnore
  @JsonView({vGrupaUczestnikList.class})
  @OrderBy("grupaPrzedszkolna")
  private List<Uczestnik> uczestnicy;
  
  @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
  @JsonIgnore
  @JsonView({vGrupaZajeciaList.class})
  private List<Zajecia> zajecia;
  
  private String timestamp;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idRoku")
  @JsonView({Uczestnik.vUczestnikSeason.class})
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Season season;
  
  @Column(name = "nazwa")
  @JsonView({vGrupaNazwa.class})
  private String name;
  
  @JsonView({vGrupaGodziny.class})
  @Column(name = "godzinaZajecOd")
  private String classesStartTime;
  
  @JsonView({vGrupaGodziny.class})
  @Column(name = "godzinaZajecDo")
  private String classesEndTime;
  
  @Column(name = "dzienZajec")
  @JsonView({vGrupaGodziny.class})
  private int classesDay;
  
  @Column(name = "cenaZaJedneZajecia")
  @JsonView({vGrupaCenaZaJedneZajecia.class})
  private float priceForOneClass;
  
  private int sort;
  
  public static interface vGrupaRest {}
  
  public static interface vGrupaId {}
  
  public static interface vGrupaNazwa {}
  
  public static interface vGrupaGodziny {}
  
  public static interface vGrupaUczestnikList {}
  
  public static interface vGrupaZajeciaList {}
  
  public static interface vGrupaCenaZaJedneZajecia {}
}
