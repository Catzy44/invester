package me.catzy.prestiz.objects.instruktorzy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Instruktorzy")
@JsonIgnoreProperties({"haslo"})
@Data
public class Instruktor {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  
  @Column(name = "additionalInfo")
  private String additionalInfo;
  
  private String timestamp;
  
  private Integer idRoku;
  
  private boolean deleted;
  
  private String avatar;
  
  @Column(name = "imie")
  private String firstName;
  
  @Column(name = "nazwisko")
  private String lastName;
  
  @Column(name = "telefon")
  private String phoneNum;
  
  @Column(name = "nick")
  private String nick;
  
  @Column(name = "haslo")
  @JsonIgnore
  private String password;
  
  @Column(name = "rodzaj")
  private int type;
  
  @Column(name = "idAktywnegoMiejsca")
  private Integer activePlaceId;
  
  @Column(name = "idAktywnejSpolki")
  private Integer activeCompanyId;
  
  private Integer sort;
  
  private String accesableYears;
  
  private boolean isAdmin;
  
  private boolean isHidden;
  
  private boolean showService;
}
