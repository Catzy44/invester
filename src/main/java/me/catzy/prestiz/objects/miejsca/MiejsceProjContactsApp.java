package me.catzy.prestiz.objects.miejsca;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import me.catzy.prestiz.objects.seasons.Season;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "contactsApp", types = {Miejsce.class})
public interface MiejsceProjContactsApp {
  int getId();
  
  String getName();
  
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  Season getSeason();
}
