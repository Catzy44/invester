package me.catzy.prestiz.objects.sms;

public class ConfirmationSMSRequest {
  public String phone;
  
  public String captcha;
  
  public String getPhone() {
    return this.phone;
  }
  
  public String getCaptcha() {
    return this.captcha;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public void setCaptcha(String captcha) {
    this.captcha = captcha;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ConfirmationSMSRequest))
      return false; 
    ConfirmationSMSRequest other = (ConfirmationSMSRequest)o;
    if (!other.canEqual(this))
      return false; 
    Object this$phone = getPhone();
    Object other$phone = other.getPhone();
    if ((this$phone == null) ? (
      other$phone == null) : 
      
      this$phone.equals(other$phone)) {
      Object this$captcha = getCaptcha();
      Object other$captcha = other.getCaptcha();
      if (this$captcha == null) {
        if (other$captcha == null)
          return true; 
      } else if (this$captcha.equals(other$captcha)) {
        return true;
      } 
      return false;
    } 
    return false;
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ConfirmationSMSRequest;
  }
  
  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Object $phone = getPhone();
    result = result * 59 + (($phone == null) ? 43 : $phone.hashCode());
    Object $captcha = getCaptcha();
    result = result * 59 + (($captcha == null) ? 43 : $captcha.hashCode());
    return result;
  }
  
  public String toString() {
    return "ConfirmationSMSRequest(phone=" + getPhone() + ", captcha=" + getCaptcha();
  }
}
