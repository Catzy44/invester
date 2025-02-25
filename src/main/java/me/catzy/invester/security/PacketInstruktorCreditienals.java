package me.catzy.invester.security;

public class PacketInstruktorCreditienals {
  private String nick;
  
  private String password;
  
  public String getNick() {
    return this.nick;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setNick(String nick) {
    this.nick = nick;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof PacketInstruktorCreditienals))
      return false; 
    PacketInstruktorCreditienals other = (PacketInstruktorCreditienals)o;
    if (!other.canEqual(this))
      return false; 
    Object this$nick = getNick();
    Object other$nick = other.getNick();
    if ((this$nick == null) ? (
      other$nick == null) : 
      
      this$nick.equals(other$nick)) {
      Object this$password = getPassword();
      Object other$password = other.getPassword();
      if (this$password == null) {
        if (other$password == null)
          return true; 
      } else if (this$password.equals(other$password)) {
        return true;
      } 
      return false;
    } 
    return false;
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof PacketInstruktorCreditienals;
  }
  
  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Object $nick = getNick();
    result = result * 59 + (($nick == null) ? 43 : $nick.hashCode());
    Object $password = getPassword();
    result = result * 59 + (($password == null) ? 43 : $password.hashCode());
    return result;
  }
  
  public String toString() {
    return "PacketInstruktorCreditienals(nick=" + getNick() + ", password=" + getPassword();
  }
}
