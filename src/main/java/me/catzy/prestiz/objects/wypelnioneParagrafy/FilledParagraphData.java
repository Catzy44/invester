package me.catzy.prestiz.objects.wypelnioneParagrafy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Arrays;

@JsonSerialize(using = FilledParagraphDataSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilledParagraphData {
  private String imie;
  
  private String nazwisko;
  
  private int wiek;
  
  private int plec;
  
  private String email;
  
  private String telefon;
  
  public int[] groups;
  
  public boolean checked;
  
  private int code;
  
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
  
  public int[] getGroups() {
    return this.groups;
  }
  
  public boolean isChecked() {
    return this.checked;
  }
  
  public int getCode() {
    return this.code;
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
  
  public void setGroups(int[] groups) {
    this.groups = groups;
  }
  
  public void setChecked(boolean checked) {
    this.checked = checked;
  }
  
  public void setCode(int code) {
    this.code = code;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof FilledParagraphData))
      return false; 
    FilledParagraphData other = (FilledParagraphData)o;
    if (!other.canEqual(this))
      return false; 
    if (getWiek() != other.getWiek())
      return false; 
    if (getPlec() != other.getPlec())
      return false; 
    if (isChecked() != other.isChecked())
      return false; 
    if (getCode() != other.getCode())
      return false; 
    Object this$imie = getImie();
    Object other$imie = other.getImie();
    if ((this$imie == null) ? (
      other$imie == null) : 
      
      this$imie.equals(other$imie)) {
      Object this$nazwisko = getNazwisko();
      Object other$nazwisko = other.getNazwisko();
      if ((this$nazwisko == null) ? (
        other$nazwisko == null) : 
        
        this$nazwisko.equals(other$nazwisko)) {
        Object this$email = getEmail();
        Object other$email = other.getEmail();
        if ((this$email == null) ? (
          other$email == null) : 
          
          this$email.equals(other$email)) {
          Object this$telefon = getTelefon();
          Object other$telefon = other.getTelefon();
          if (this$telefon == null) {
            if (other$telefon == null)
              return Arrays.equals(getGroups(), other.getGroups()); 
          } else if (this$telefon.equals(other$telefon)) {
            return Arrays.equals(getGroups(), other.getGroups());
          } 
          return false;
        } 
        return false;
      } 
      return false;
    } 
    return false;
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof FilledParagraphData;
  }
  
  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    result = result * 59 + getWiek();
    result = result * 59 + getPlec();
    result = result * 59 + (isChecked() ? 79 : 97);
    result = result * 59 + getCode();
    Object $imie = getImie();
    result = result * 59 + (($imie == null) ? 43 : $imie.hashCode());
    Object $nazwisko = getNazwisko();
    result = result * 59 + (($nazwisko == null) ? 43 : $nazwisko.hashCode());
    Object $email = getEmail();
    result = result * 59 + (($email == null) ? 43 : $email.hashCode());
    Object $telefon = getTelefon();
    result = result * 59 + (($telefon == null) ? 43 : $telefon.hashCode());
    result = result * 59 + Arrays.hashCode(getGroups());
    return result;
  }
  
  public String toString() {
    return "FilledParagraphData(imie=" + getImie() + ", nazwisko=" + getNazwisko() + ", wiek=" + getWiek() + ", plec=" + getPlec() + ", email=" + getEmail() + ", telefon=" + getTelefon() + ", groups=" + Arrays.toString(getGroups()) + ", checked=" + isChecked() + ", code=" + getCode();
  }
}
