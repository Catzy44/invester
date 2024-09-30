package me.catzy.prestiz.objects.uczestnicy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.uczestnicy.personalData.AttenderPersonalInfo;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "contactsApp", types = {Uczestnik.class})
public interface UczestnikProjContactsApp {
  int getId();
  
  @JsonIdentityReference(alwaysAsId = true)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  Season getSeason();
  
  @JsonIdentityReference(alwaysAsId = true)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  Miejsce getPlace();
  
  AttenderPersonalInfo getAttenderInfo();
  
  AttenderPersonalInfo getParentInfo();
}
