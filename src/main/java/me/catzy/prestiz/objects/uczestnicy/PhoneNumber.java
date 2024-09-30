package me.catzy.prestiz.objects.uczestnicy;

public class PhoneNumber {
  private String number;
  
  private String numberFixed;
  
  private boolean isValid;
  
  public PhoneNumber(String s) {
    this.isValid = false;
    this.number = s;
    if (s == null) {
      this.numberFixed = null;
      this.isValid = false;
      return;
    } 
    if (s.startsWith(" 48") && s.length() == 12) {
      this.numberFixed = "+48" + s.substring(2);
      this.isValid = true;
    } 
    s = s.replace(" ", "");
    s = s.replace("-", "");
    int len = s.length();
    if (len == 9) {
      this.numberFixed = "+48" + s;
      this.isValid = true;
    } else if (len == 12) {
      this.numberFixed = s;
      this.isValid = true;
    } 
  }
  
  public String getRawNumber() {
    return this.number;
  }
  
  public String getNumberFixed() {
    return this.numberFixed;
  }
  
  public void setNumberFixed(String numberFixed) {
    this.numberFixed = numberFixed;
  }
  
  public boolean isValid() {
    return this.isValid;
  }
  
  public void setValid(boolean isValid) {
    this.isValid = isValid;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof PhoneNumber))
      return false; 
    PhoneNumber pn = (PhoneNumber)obj;
    return pn.numberFixed.equals(this.numberFixed);
  }
}
