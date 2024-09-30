package me.catzy.prestiz.objects.umowy;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import me.catzy.prestiz.objects.formularze.Formularz;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.wypelnioneParagrafy.WypelnionyParagraf;

@Entity
@Table(name = "form_umowy")
public class Umowa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @OneToMany(mappedBy = "umowa")
  private List<WypelnionyParagraf> wypelnioneParagrafy;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idUczestnika")
  @JsonIgnore
  private Uczestnik uczestnik;
  
  private Timestamp created;
  
  private int data;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "popupId")
  private Formularz formularz;
  
  public int getId() {
    return this.id;
  }
  
  public List<WypelnionyParagraf> getWypelnioneParagrafy() {
    return this.wypelnioneParagrafy;
  }
  
  public Uczestnik getUczestnik() {
    return this.uczestnik;
  }
  
  public Timestamp getCreated() {
    return this.created;
  }
  
  public int getData() {
    return this.data;
  }
  
  public Formularz getFormularz() {
    return this.formularz;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setWypelnioneParagrafy(List<WypelnionyParagraf> wypelnioneParagrafy) {
    this.wypelnioneParagrafy = wypelnioneParagrafy;
  }
  
  @JsonIgnore
  public void setUczestnik(Uczestnik uczestnik) {
    this.uczestnik = uczestnik;
  }
  
  public void setCreated(Timestamp created) {
    this.created = created;
  }
  
  public void setData(int data) {
    this.data = data;
  }
  
  public void setFormularz(Formularz formularz) {
    this.formularz = formularz;
  }
}
