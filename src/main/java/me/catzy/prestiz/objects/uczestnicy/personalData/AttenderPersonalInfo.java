package me.catzy.prestiz.objects.uczestnicy.personalData;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "personal_info")
@JsonView({AttenderPersonalInfo.vAttenderPersonalInfoRest.class})
public class AttenderPersonalInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({vAttenderPersonalInfoId.class})
  private int id;
  
  @Column(name = "first_name")
  private String firstName;
  
  @Column(name = "middle_name")
  private String middleName;
  
  @Column(name = "last_name")
  private String lastName;
  
  @Column(name = "number")
  private String number;
  
  @Column(name = "alt_number")
  private String altNumber;
  
  @Column(name = "email")
  private String email;
  
  @Column(name = "alt_email")
  private String altEmail;
  
  @Column(name = "address_id")
  private Integer addressId;
  
  public int getId() {
    return this.id;
  }
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public String getMiddleName() {
    return this.middleName;
  }
  
  public String getLastName() {
    return this.lastName;
  }
  
  public String getNumber() {
    return this.number;
  }
  
  public String getAltNumber() {
    return this.altNumber;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public String getAltEmail() {
    return this.altEmail;
  }
  
  public Integer getAddressId() {
    return this.addressId;
  }
  
  @JsonView({vAttenderPersonalInfoId.class})
  public void setId(int id) {
    this.id = id;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public void setAltNumber(String altNumber) {
    this.altNumber = altNumber;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public void setAltEmail(String altEmail) {
    this.altEmail = altEmail;
  }
  
  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }
  
  public static interface vAttenderPersonalInfoId {}
  
  public static interface vAttenderPersonalInfoRest {}
  
  public static interface vAttenderPersonalInfoFull extends vAttenderPersonalInfoRest, vAttenderPersonalInfoId {}
}
