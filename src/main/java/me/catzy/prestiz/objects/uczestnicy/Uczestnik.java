package me.catzy.prestiz.objects.uczestnicy;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.Hibernate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import me.catzy.prestiz.objects.attendersGroupChanges.AttenderGroupChange;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.miasta.closedMonths.ClosedMonth;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.sms.SMS;
import me.catzy.prestiz.objects.uczestnicy.personalData.AttenderPersonalInfo;
import me.catzy.prestiz.objects.umowy.Umowa;
import me.catzy.prestiz.objects.zajecia.presences.Presence;

@Entity
@Table(name = "Uczestnicy")
@JsonView({Uczestnik.vUczestnikRest.class})
@Getter
@Setter
public class Uczestnik {
  @Transient
  private PhoneNumber phoneNumberParsed;
  
  @JsonView({vUczestnikId.class})
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idMiejsca")
  @JsonView({vUczestnikMiejsce.class})
  private Miejsce place;
  
  @Column(name = "idMiejsca", insertable = false, updatable = false)
  private int placeId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idGrupy")
  @JsonView({vUczestnikGrupa.class})
  private Grupa group;
  
  @Column(name = "idGrupy", insertable = false, updatable = false)
  private int groupId;
  
  @OneToMany(mappedBy = "uczestnik", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<SMS> smsy;
  
  private Timestamp timestamp;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idRoku")
  @JsonView({vUczestnikSeason.class})
  private Season season;
  
  @OneToMany(mappedBy = "uczestnik")
  @JsonIgnore
  private List<Umowa> umowy;
  
  @OneToMany(mappedBy = "attender")
  @JsonIgnore
  private List<AttenderGroupChange> groupChanges;
  
  @OneToMany(mappedBy = "attender")
  private List<Presence> presences;
  
  @JsonView({vUczestnikClosedMonths.class})
  @OneToMany(mappedBy = "attender")
  @JsonIgnore
  private List<ClosedMonth> closedMonths;
  
  @JsonView({vUczestnikName.class})
  private String imie;
  
  @JsonView({vUczestnikName.class})
  private String nazwisko;
  
  private int plec;
  
  private Integer wiek;
  
  private int umowa;
  
  @Column(name = "dataDodania")
  private LocalDate dateAdded;
  
  @Column(name = "dataDolaczenia")
  private LocalDate dateJoined;
  
  private String dataPodpisaniaUmowy;
  
  private String imieOpiekun;
  
  private String nazwiskoOpiekun;
  
  private String imiePartner;
  
  private String nazwiskoPartner;
  
  private String telefonPartner;
  
  private String adresOpiekuna;
  
  @JsonView({vUczestnikTel.class})
  private String telefonOpiekuna;
  
  private String emailOpiekuna;
  
  private int rabatPrc;
  
  private String zawieszenie;
  
  private String notatkaDoWiadomosciFirmy;
  
  private String notatkaDlaRodzicow;
  
  private String grupaPrzedszkolna;
  
  @Column(name = "interakcja")
  private boolean interactionIsNeeded;
  
  @Column(name = "kolor")
  private String custColor;
  
  @Column(name = "kolorAktywny")
  private boolean custColorIsActive;
  
  private boolean approved;
  
  private boolean individualPayment;
  
  @JsonView({vUczestnikPersonalInfo.class})
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attender_id")
  private AttenderPersonalInfo attenderInfo;
  
  @JsonView({vUczestnikPersonalInfo.class})
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private AttenderPersonalInfo parentInfo;
  
  public Uczestnik() {}
  
  public Uczestnik(Integer id, String imie, String nazwisko, Miejsce miejsce, Season season) {
    this.id = id.intValue();
    this.imie = imie;
    this.nazwisko = nazwisko;
    this.place = miejsce;
    this.season = season;
  }
  
  public Uczestnik(Integer id, String imie, String nazwisko) {
    this.id = id.intValue();
    this.imie = imie;
    this.nazwisko = nazwisko;
  }
  
  public Uczestnik(Integer id, String telefonOpiekuna) {
    this.id = id.intValue();
    this.telefonOpiekuna = telefonOpiekuna;
  }
  
  public Uczestnik copySomeOfDataIdk() {
    Uczestnik u = new Uczestnik();
    u.setPlace(this.place);
    u.setGroup(this.group);
    u.setImie(this.imie);
    u.setNazwisko(this.nazwisko);
    u.setPlec(this.plec);
    u.setWiek(this.wiek);
    u.setImieOpiekun(this.imieOpiekun);
    u.setNazwiskoOpiekun(this.nazwiskoOpiekun);
    u.setImiePartner(this.imiePartner);
    u.setNazwiskoPartner(this.nazwiskoPartner);
    u.setTelefonPartner(this.telefonPartner);
    u.setAdresOpiekuna(this.adresOpiekuna);
    u.setTelefonOpiekuna(this.telefonOpiekuna);
    u.setEmailOpiekuna(this.emailOpiekuna);
    u.setApproved(this.approved);
    return u;
  }
  
  @JsonView({vUczestnikGrupa.class})
  public Grupa getGroup() {
    if (this.group.getId() == 0 || this.group.getId() == -1)
      return null; 
    try {
      if (!Hibernate.isInitialized(this.group))
        Hibernate.initialize(this.group); 
    } catch (Exception one) {
      this.group = null;
    } 
    return this.group;
  }
  
  public static interface vUczestnikTel {}
  
  public static interface vUczestnikId {}
  
  public static interface vUczestnikName {}
  
  public static interface vUczestnikRest {}
  
  public static interface vUczestnikFull extends vUczestnikRest, vUczestnikName, vUczestnikId, vUczestnikTel {}
  
  public static interface vUczestnikMiejsce {}
  
  public static interface vUczestnikGrupa {}
  
  public static interface vUczestnikSeason {}
  
  public static interface vUczestnikClosedMonths {}
  
  public static interface vUczestnikPersonalInfo {}
}
