package me.catzy.prestiz.objects.miejsca;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
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
import me.catzy.prestiz.objects.formularze.Formularz;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.miasta.Miasto;
import me.catzy.prestiz.objects.platnosci.Payment;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.sms_group.SMSGroup;
import me.catzy.prestiz.objects.spolki.Spolka;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Entity
@Table(name = "Miejsca")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonView({Miejsce.vMiejsceRest.class})
public class Miejsce {
	public static class PlaceType {
		  public static final int KIDS = 0;
		  public static final int PLACE = 1;
		  public static final int ADULTS = 2;
	  }
  
  @Id
  @Access(AccessType.PROPERTY)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({vMiejsceId.class})
  private int id;
  
  @OneToMany(mappedBy = "place")
  @JsonIgnore
  private List<SMSGroup> smsGroups;
  
  @OneToMany(mappedBy = "place")
  @JsonIgnore
  @JsonView({vMiejsceGrupy.class})
  @OrderBy("sort DESC")
  private List<Grupa> groups;
  
  @OneToMany(mappedBy = "place")
  @JsonIgnore
  private List<Formularz> formularze;
  
  @OneToMany(mappedBy = "place")
  @JsonIgnore
  private List<Uczestnik> uczestnicy;
  
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city")
  @JsonView({vMiejsceMiasto.class})
  private Miasto miasto;
  
  @Column(name = "rodzaj")
  private int type;
  
  private String timestamp;
  
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idRoku")
  @JsonView({Uczestnik.vUczestnikSeason.class})
  private Season season;
  
  @JsonView({vMiejsceNazwa.class})
  @Column(name = "nazwa")
  private String name;
  
  @Column(name = "additionalInfo")
  private String additionalInfo;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idInstruktora")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Instruktor instructor;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idSpolki")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Spolka company;
  
  @OneToMany(mappedBy = "place")
  @JsonIgnore
  private List<Payment> payments;
  
  @Column(name = "progi")
  private String thresholds;
  
  @JsonView({vMiejsceAdres.class})
  @Column(name = "adres")
  private String address;
  
  @Column(name = "adres1")
  private String address1;
  
  @Column(name = "adresLink")
  private String addressUrl;
  
  @Column(name = "dyrektorImie")
  private String directorFirstname;
  
  @Column(name = "dyrektorNazwisko")
  private String directorLastname;
  
  @Column(name = "telefon")
  private String phoneNum;
  
  @Column(name = "domyslnyDzienZajec")
  private int defaultClassWeekDay;
  
  @Column(name = "domyslnaCenaZaJedneZajeciaDlaGrup")
  private float defaultPriceForOneClass;
  
  @Column(name = "notatka")
  private String note;
  
  @Column(name = "placowkaNrKonta")
  private String placeBankAccNum;
  
  @Column(name = "rodzajModyfikacjiDochodu")
  private int incomeModificationType;
  
  @Column(name = "modyfikacjaDochodu")
  private int indomeModification;
  
  
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public int getId() {
		return this.id;
	}

  public List<SMSGroup> getSmsGroups() {
    return this.smsGroups;
  }
  
  public List<Grupa> getGroups() {
    return this.groups;
  }
  
  public List<Formularz> getFormularze() {
    return this.formularze;
  }
  
  public List<Uczestnik> getUczestnicy() {
    return this.uczestnicy;
  }
  
  public Miasto getMiasto() {
    return this.miasto;
  }
  
  public int getType() {
    return this.type;
  }
  
  public String getTimestamp() {
    return this.timestamp;
  }
  
  public Season getSeason() {
    return this.season;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Instruktor getInstructor() {
    return this.instructor;
  }
  
  public Spolka getCompany() {
    return this.company;
  }
  
  public List<Payment> getPayments() {
    return this.payments;
  }
  
  public String getThresholds() {
    return this.thresholds;
  }
  
  public String getAddress() {
    return this.address;
  }
  
  public String getAddress1() {
    return this.address1;
  }
  
  public String getAddressUrl() {
    return this.addressUrl;
  }
  
  public String getDirectorFirstname() {
    return this.directorFirstname;
  }
  
  public String getDirectorLastname() {
    return this.directorLastname;
  }
  
  public String getPhoneNum() {
    return this.phoneNum;
  }
  
  public int getDefaultClassWeekDay() {
    return this.defaultClassWeekDay;
  }
  
  public float getDefaultPriceForOneClass() {
    return this.defaultPriceForOneClass;
  }
  
  public String getNote() {
    return this.note;
  }
  
  public String getPlaceBankAccNum() {
    return this.placeBankAccNum;
  }
  
  public int getIncomeModificationType() {
    return this.incomeModificationType;
  }
  
  public int getIndomeModification() {
    return this.indomeModification;
  }
  
  @JsonView({vMiejsceId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  @JsonIgnore
  public void setSmsGroups(List<SMSGroup> smsGroups) {
    this.smsGroups = smsGroups;
  }
  
  @JsonIgnore
  @JsonView({vMiejsceGrupy.class})
  public void setGroups(List<Grupa> groups) {
    this.groups = groups;
  }
  
  @JsonIgnore
  public void setFormularze(List<Formularz> formularze) {
    this.formularze = formularze;
  }
  
  @JsonIgnore
  public void setUczestnicy(List<Uczestnik> uczestnicy) {
    this.uczestnicy = uczestnicy;
  }
  
  @JsonView({vMiejsceMiasto.class})
  public void setMiasto(Miasto miasto) {
    this.miasto = miasto;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  
  @JsonView({Uczestnik.vUczestnikSeason.class})
  public void setSeason(Season season) {
    this.season = season;
  }
  
  @JsonView({vMiejsceNazwa.class})
  public void setName(String name) {
    this.name = name;
  }
  
  public void setInstructor(Instruktor instructor) {
    this.instructor = instructor;
  }
  
  public void setCompany(Spolka company) {
    this.company = company;
  }
  
  @JsonIgnore
  public void setPayments(List<Payment> payments) {
    this.payments = payments;
  }
  
  public void setThresholds(String thresholds) {
    this.thresholds = thresholds;
  }
  
  @JsonView({vMiejsceAdres.class})
  public void setAddress(String address) {
    this.address = address;
  }
  
  public void setAddress1(String address1) {
    this.address1 = address1;
  }
  
  public void setAddressUrl(String addressUrl) {
    this.addressUrl = addressUrl;
  }
  
  public void setDirectorFirstname(String directorFirstname) {
    this.directorFirstname = directorFirstname;
  }
  
  public void setDirectorLastname(String directorLastname) {
    this.directorLastname = directorLastname;
  }
  
  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }
  
  public void setDefaultClassWeekDay(int defaultClassWeekDay) {
    this.defaultClassWeekDay = defaultClassWeekDay;
  }
  
  public void setDefaultPriceForOneClass(float defaultPriceForOneClass) {
    this.defaultPriceForOneClass = defaultPriceForOneClass;
  }
  
  public void setNote(String note) {
    this.note = note;
  }
  
  public void setPlaceBankAccNum(String placeBankAccNum) {
    this.placeBankAccNum = placeBankAccNum;
  }
  
  public void setIncomeModificationType(int incomeModificationType) {
    this.incomeModificationType = incomeModificationType;
  }
  
  public void setIndomeModification(int indomeModification) {
    this.indomeModification = indomeModification;
  }
  
  public static interface vMiejsceRest {}
  
  public static interface vMiejsceId {}
  
  public static interface vMiejsceMiasto {}
  
  public static interface vMiejsceAdres {}
  
  public static interface vMiejsceNazwa {}
  
  public static interface vMiejsceGrupy {}
}
