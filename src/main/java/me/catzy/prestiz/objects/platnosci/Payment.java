package me.catzy.prestiz.objects.platnosci;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Entity
@Table(name = "Platnosci")
@JsonView({Payment.vPaymentRest.class})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment {
  @JsonView({vPaymentId.class})
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  private Timestamp timestamp;
  
  private int idRoku;
  
  private int idWyciagu;
  
  @ManyToOne
  @JoinColumn(name = "idUczestnika")
  private Uczestnik attender;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idPlacowki")
  private Miejsce place;
  
  private int idSpolki;
  
  private String data;
  
  private String tytul;
  
  private String nadawcaOdbiorca;
  
  @Column(name = "kwota")
  private float amount;
  
  private String nrKonta;
  
  private boolean ukryta;
  
  public int getId() {
    return this.id;
  }
  
  public Timestamp getTimestamp() {
    return this.timestamp;
  }
  
  public int getIdRoku() {
    return this.idRoku;
  }
  
  public int getIdWyciagu() {
    return this.idWyciagu;
  }
  
  public Uczestnik getAttender() {
    return this.attender;
  }
  
  public Miejsce getPlace() {
    return this.place;
  }
  
  public int getIdSpolki() {
    return this.idSpolki;
  }
  
  public String getData() {
    return this.data;
  }
  
  public String getTytul() {
    return this.tytul;
  }
  
  public String getNadawcaOdbiorca() {
    return this.nadawcaOdbiorca;
  }
  
  public float getAmount() {
    return this.amount;
  }
  
  public String getNrKonta() {
    return this.nrKonta;
  }
  
  public boolean isUkryta() {
    return this.ukryta;
  }
  
  @JsonView({vPaymentId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
  
  public void setIdRoku(int idRoku) {
    this.idRoku = idRoku;
  }
  
  public void setIdWyciagu(int idWyciagu) {
    this.idWyciagu = idWyciagu;
  }
  
  public void setAttender(Uczestnik attender) {
    this.attender = attender;
  }
  
  public void setPlace(Miejsce place) {
    this.place = place;
  }
  
  public void setIdSpolki(int idSpolki) {
    this.idSpolki = idSpolki;
  }
  
  public void setData(String data) {
    this.data = data;
  }
  
  public void setTytul(String tytul) {
    this.tytul = tytul;
  }
  
  public void setNadawcaOdbiorca(String nadawcaOdbiorca) {
    this.nadawcaOdbiorca = nadawcaOdbiorca;
  }
  
  public void setAmount(float amount) {
    this.amount = amount;
  }
  
  public void setNrKonta(String nrKonta) {
    this.nrKonta = nrKonta;
  }
  
  public void setUkryta(boolean ukryta) {
    this.ukryta = ukryta;
  }
  
  public static interface vPaymentId {}
  
  public static interface vPaymentRest {}
  
  public static interface vPaymentFull extends vPaymentRest, vPaymentId {}
}
