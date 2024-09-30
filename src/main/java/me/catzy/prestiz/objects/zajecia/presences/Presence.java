package me.catzy.prestiz.objects.zajecia.presences;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.zajecia.Zajecia;

@Entity
@Table(name = "ClassPresences")
@JsonView({Presence.vPresenceRest.class})
@Data
public class Presence {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView({vPresenceId.class})
  private int id;
  
  @ManyToOne
  @JoinColumn(name = "idClass")
  @JsonIgnore
  private Zajecia classes;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idAttender")
  //@JsonIgnore
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @JsonView({vPresenceAttender.class})
  private Uczestnik attender;
  
  @JsonView({vPresencePresence.class})
  private int presence;
  
  public static interface vPresenceRest {}
  public static interface vPresenceId {}
  public static interface vPresenceData {}
  public static interface vPresenceAttender {}
  public static interface vPresencePresence {}
}
