package me.catzy.prestiz.objects.sms;

public class SearchForSMS {
  private String name;
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof SearchForSMS))
      return false; 
    SearchForSMS other = (SearchForSMS)o;
    if (!other.canEqual(this))
      return false; 
    Object this$name = getName();
    Object other$name = other.getName();
    if (this$name == null) {
      if (other$name == null)
        return true; 
    } else if (this$name.equals(other$name)) {
      return true;
    } 
    return false;
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof SearchForSMS;
  }
  
  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Object $name = getName();
    result = result * 59 + (($name == null) ? 43 : $name.hashCode());
    return result;
  }
  
  public String toString() {
    return "SearchForSMS(name=" + getName();
  }
}
