package me.catzy.prestiz.objects.miasta.closedMonths;

class ClosedMonthD {
  int attenderId;
  
  int month = -1;
  
  int cycle = -1;
  
  float amount;
  
  public int getAttenderId() {
    return this.attenderId;
  }
  
  public int getMonth() {
    return this.month;
  }
  
  public int getCycle() {
    return this.cycle;
  }
  
  public float getAmount() {
    return this.amount;
  }
  
  public void setAttenderId(int attenderId) {
    this.attenderId = attenderId;
  }
  
  public void setMonth(int month) {
    this.month = month;
  }
  
  public void setCycle(int cycle) {
    this.cycle = cycle;
  }
  
  public void setAmount(float amount) {
    this.amount = amount;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ClosedMonthD))
      return false; 
    ClosedMonthD other = (ClosedMonthD)o;
    return (other.canEqual(this) && getAttenderId() == other.getAttenderId() && getMonth() == other.getMonth() && getCycle() == other.getCycle() && Float.compare(getAmount(), other.getAmount()) == 0);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ClosedMonthD;
  }
  
  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    result = result * 59 + getAttenderId();
    result = result * 59 + getMonth();
    result = result * 59 + getCycle();
    result = result * 59 + Float.floatToIntBits(getAmount());
    return result;
  }
  
  public String toString() {
    return "ClosedMonthController.ClosedMonthD(attenderId=" + getAttenderId() + ", month=" + getMonth() + ", cycle=" + getCycle() + ", amount=" + getAmount();
  }
}
