package me.catzy.prestiz.objects.uczestnicy_dane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import me.catzy.prestiz.objects.wypelnioneParagrafy.FilledParagraphData;

@Entity
@Table(name = "UczestnicyDane")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UczestnikDane {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  private String imie;
  
  private String nazwisko;
  
  private int wiek;
  
  private int plec;
  
  private String email;
  
  private String telefon;
  
  public UczestnikDane(FilledParagraphData par) {
    this.imie = par.getImie();
    this.nazwisko = par.getNazwisko();
    this.wiek = par.getWiek();
    this.plec = par.getPlec();
    this.email = par.getEmail();
    this.telefon = par.getTelefon();
  }
  
  public UczestnikDane() {}
  
  public int getId() {
    return this.id;
  }
  
  public String getImie() {
    return this.imie;
  }
  
  public String getNazwisko() {
    return this.nazwisko;
  }
  
  public int getWiek() {
    return this.wiek;
  }
  
  public int getPlec() {
    return this.plec;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public String getTelefon() {
    return this.telefon;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setImie(String imie) {
    this.imie = imie;
  }
  
  public void setNazwisko(String nazwisko) {
    this.nazwisko = nazwisko;
  }
  
  public void setWiek(int wiek) {
    this.wiek = wiek;
  }
  
  public void setPlec(int plec) {
    this.plec = plec;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public void setTelefon(String telefon) {
    this.telefon = telefon;
  }
}
