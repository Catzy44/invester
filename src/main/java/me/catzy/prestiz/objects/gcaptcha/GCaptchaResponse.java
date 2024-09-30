package me.catzy.prestiz.objects.gcaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GCaptchaResponse {
  public boolean success;
  
  public String challenge_ts;
  
  public String hostname;
  
  @JsonProperty("error-codes")
  public String error_codes;
  
  public String toString() {
    return "GCaptchaResponse [success=" + this.success + ", challenge_ts=" + this.challenge_ts + ", hostname=" + this.hostname + ", error_codes=" + this.error_codes;
  }
  
  public boolean isSuccess() {
    return this.success;
  }
  
  public String getChallenge_ts() {
    return this.challenge_ts;
  }
  
  public String getHostname() {
    return this.hostname;
  }
  
  public String getError_codes() {
    return this.error_codes;
  }
  
  public void setSuccess(boolean success) {
    this.success = success;
  }
  
  public void setChallenge_ts(String challenge_ts) {
    this.challenge_ts = challenge_ts;
  }
  
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }
  
  @JsonProperty("error-codes")
  public void setError_codes(String error_codes) {
    this.error_codes = error_codes;
  }
}
