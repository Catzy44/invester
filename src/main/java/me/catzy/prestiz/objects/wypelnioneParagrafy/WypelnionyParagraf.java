package me.catzy.prestiz.objects.wypelnioneParagrafy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import me.catzy.prestiz.objects.paragrafy.Paragraf;
import me.catzy.prestiz.objects.umowy.Umowa;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "form_dane")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WypelnionyParagraf {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  private int type;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "paragrafId")
  @JsonDeserialize(using = ParagrafDeserializer.class)
  private Paragraf paragraf;
  
  @ManyToOne
  @JoinColumn(name = "umowaId")
  @JsonIgnore
  private Umowa umowa;
  
  @JdbcTypeCode(3001)
  private FilledParagraphData data;
  
  public int getId() {
    return this.id;
  }
  
  public int getType() {
    return this.type;
  }
  
  public Paragraf getParagraf() {
    return this.paragraf;
  }
  
  public Umowa getUmowa() {
    return this.umowa;
  }
  
  public FilledParagraphData getData() {
    return this.data;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  @JsonDeserialize(using = ParagrafDeserializer.class)
  public void setParagraf(Paragraf paragraf) {
    this.paragraf = paragraf;
  }
  
  @JsonIgnore
  public void setUmowa(Umowa umowa) {
    this.umowa = umowa;
  }
  
  public void setData(FilledParagraphData data) {
    this.data = data;
  }
}
