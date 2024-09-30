package me.catzy.prestiz.objects.sesje;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;

@Entity
@Table(name = "Sesje")
public class Sesja {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  private String session_id;
  
  @ManyToOne
  @JoinColumn(name = "idInstruktora")
  @JsonIgnore
  private Instruktor instruktor;
  
  private Timestamp first_login_date;
  
  private Timestamp last_login_date;
  
  private boolean active;
  
  public int getId() {
    return this.id;
  }
  
  public String getSession_id() {
    return this.session_id;
  }
  
  public Instruktor getInstruktor() {
    return this.instruktor;
  }
  
  public Timestamp getFirst_login_date() {
    return this.first_login_date;
  }
  
  public Timestamp getLast_login_date() {
    return this.last_login_date;
  }
  
  public boolean isActive() {
    return this.active;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setSession_id(String session_id) {
    this.session_id = session_id;
  }
  
  @JsonIgnore
  public void setInstruktor(Instruktor instruktor) {
    this.instruktor = instruktor;
  }
  
  public void setFirst_login_date(Timestamp first_login_date) {
    this.first_login_date = first_login_date;
  }
  
  public void setLast_login_date(Timestamp last_login_date) {
    this.last_login_date = last_login_date;
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
}
