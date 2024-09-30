package me.catzy.prestiz.objects.miasta;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import me.catzy.prestiz.objects.miejsca.Miejsce;

@Entity
@Table(name = "Miasta")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonView({Miasto.vMiastoRest.class})
public class Miasto {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({vMiastoId.class})
  private int id;
  
  @OneToMany(mappedBy = "miasto", fetch = FetchType.LAZY)
  @JsonBackReference
  private List<Miejsce> miejsca;
  
  @JsonView({vMiastoName.class})
  private String name;
  
  public int getId() {
    return this.id;
  }
  
  public List<Miejsce> getMiejsca() {
    return this.miejsca;
  }
  
  public String getName() {
    return this.name;
  }
  
  @JsonView({vMiastoId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  public void setMiejsca(List<Miejsce> miejsca) {
    this.miejsca = miejsca;
  }
  
  @JsonView({vMiastoName.class})
  public void setName(String name) {
    this.name = name;
  }
  
  public static interface vMiastoRest {}
  
  public static interface vMiastoId {}
  
  public static interface vMiastoName {}
}
