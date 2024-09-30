package me.catzy.prestiz.objects.miejsca;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;

@Projection(name = "withGroups", types = {Miejsce.class})
public interface MiejsceProjWIthGroups {
  int getId();
  
  String getName();
  
  String getAddress();
  
  String getDirectorFirstname();
  
  String getDirectorLastname();
  
  String getPhoneNum();
  
  int getDefaultClassWeekDay();
  
  int getType();
  
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  Instruktor getInstructor();
  
  List<Grupa> getGroups();
}
