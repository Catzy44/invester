package me.catzy.prestiz.objects.formularze;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import java.util.List;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.paragrafy.Paragraf;

@Entity
@Table(name = "form_popupy")
@JsonView({Formularz.vFormularzRest.class})
public class Formularz {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView({vFormularzId.class})
  private int id;
  
  @OrderBy("ord")
  @OneToMany(mappedBy = "formularz", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Paragraf> paragrafy;
  
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "miejsceId")
  @JsonView({vFormularzMiejsce.class})
  private Miejsce place;
  
  private boolean active;
  
  @JsonView({vFormularzName.class})
  private String name;
  
  public int getId() {
    return this.id;
  }
  
  public List<Paragraf> getParagrafy() {
    return this.paragrafy;
  }
  
  public Miejsce getPlace() {
    return this.place;
  }
  
  public boolean isActive() {
    return this.active;
  }
  
  public String getName() {
    return this.name;
  }
  
  @JsonView({vFormularzId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  @JsonIgnore
  public void setParagrafy(List<Paragraf> paragrafy) {
    this.paragrafy = paragrafy;
  }
  
  @JsonView({vFormularzMiejsce.class})
  public void setPlace(Miejsce place) {
    this.place = place;
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
  
  @JsonView({vFormularzName.class})
  public void setName(String name) {
    this.name = name;
  }
  
  public static interface vFormularzRest {}
  
  public static interface vFormularzId {}
  
  public static interface vFormularzMiejsce {}
  
  public static interface vFormularzName {}
}
