package me.catzy.prestiz.objects.attendersGroupChanges;

import java.time.LocalDate;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Entity
@Table(name = "UczestnicyWGrupachTimestampy")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class AttenderGroupChange {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private int idRoku;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idUczestnika")
  @JsonIgnore
  private Uczestnik attender;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idGrupy")
  @NotFound(action = NotFoundAction.IGNORE)
  private Grupa group;
  
  @Column(name = "aktywne")
  private boolean active;
  
  @Column(name = "dataOd")
  private LocalDate dateJoined;
  
  @Column(name = "dataDo")
  private LocalDate dateLeft;
}
