package me.catzy.prestiz.objects.sms_templates;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Wzory")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class SMSTemplate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(name = "typ")
  private int type;
  
  @Column(name = "nr")
  private Integer nr;
  
  private String text;
  
  @Column(name = "nazwa")
  private String label;
}
