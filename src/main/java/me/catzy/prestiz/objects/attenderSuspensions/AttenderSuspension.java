package me.catzy.prestiz.objects.attenderSuspensions;

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
import java.sql.Date;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Entity
@Table(name = "OkresyZawieszaniaUczestnikow")
@JsonView({Grupa.vGrupaRest.class})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AttenderSuspension {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  
  private int idRoku;
  
  @ManyToOne
  @JoinColumn(name = "idUczestnika")
  private Uczestnik attender;
  
  @Column(name = "dataOd")
  private Date dateFrom;
  
  @Column(name = "dataDo")
  private Date dateTo;
  
  public int getId() {
    return this.id;
  }
  
  public int getIdRoku() {
    return this.idRoku;
  }
  
  public Uczestnik getAttender() {
    return this.attender;
  }
  
  public Date getDateFrom() {
    return this.dateFrom;
  }
  
  public Date getDateTo() {
    return this.dateTo;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setIdRoku(int idRoku) {
    this.idRoku = idRoku;
  }
  
  public void setAttender(Uczestnik attender) {
    this.attender = attender;
  }
  
  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }
  
  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }
}
