package me.catzy.prestiz.objects.paragrafy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import me.catzy.prestiz.objects.formularze.Formularz;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "form_paragrafy")
@JsonView({Paragraf.vParagrafRest.class})
public class Paragraf {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView({vParagrafId.class})
  private int id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "popupId")
  @JsonIgnore
  @JsonDeserialize(using = FormularzDeserializer.class)
  private Formularz formularz;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "miejsceId")
  @JsonIgnore
  private Miejsce miejsce;
  
  @JsonView({vParagrafType.class})
  private int type;
  
  @JsonView({vParagrafName.class})
  private String name;
  
  @JdbcTypeCode(3001)
  @JsonView({vParagrafData.class})
  private ParagrafData data = null;
  
  @JsonIgnore
  private int ord;
  
  public int getId() {
    return this.id;
  }
  
  public Formularz getFormularz() {
    return this.formularz;
  }
  
  public Miejsce getMiejsce() {
    return this.miejsce;
  }
  
  public int getType() {
    return this.type;
  }
  
  public String getName() {
    return this.name;
  }
  
  public ParagrafData getData() {
    return this.data;
  }
  
  public int getOrd() {
    return this.ord;
  }
  
  @JsonView({vParagrafId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  @JsonIgnore
  @JsonDeserialize(using = FormularzDeserializer.class)
  public void setFormularz(Formularz formularz) {
    this.formularz = formularz;
  }
  
  @JsonIgnore
  public void setMiejsce(Miejsce miejsce) {
    this.miejsce = miejsce;
  }
  
  @JsonView({vParagrafType.class})
  public void setType(int type) {
    this.type = type;
  }
  
  @JsonView({vParagrafName.class})
  public void setName(String name) {
    this.name = name;
  }
  
  @JsonView({vParagrafData.class})
  public void setData(ParagrafData data) {
    this.data = data;
  }
  
  @JsonIgnore
  public void setOrd(int ord) {
    this.ord = ord;
  }
  
  public static interface vParagrafRest {}
  
  public static interface vParagrafId {}
  
  public static interface vParagrafType {}
  
  public static interface vParagrafName {}
  
  public static interface vParagrafData {}
}
