package me.catzy.prestiz.objects.attendersGroupChanges;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.grupy.GrupyService;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Service
public class AttenderGroupChangesService {
  @Autowired
  private AttenderGroupChangesRepository repo;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private UczestnicyService serviceAttender;
  
  @Autowired
  private GrupyService serviceGroups;
  
  @Cacheable(value = {"grupy"}, key = "#id", condition = "#id != 0")
  public AttenderGroupChange byId(int id) {
    return this.repo.getById(id);
  }
  
  public AttenderGroupChange[] getByIds(int[] ids) {
    if (ids.length == 0)
      return null; 
    return this.repo.getByIdIn(ids);
  }
  
  public AttenderGroupChange patch(Map<Object, Object> map) throws UserException, JsonMappingException {
    int id = -1;
    for (Object k_ : map.keySet()) {
      String k = (String)k_;
      if (k.equals("id")) {
        id = ((Integer)map.get(k)).intValue();
        break;
      } 
    } 
    if (id == -1)
      throw new UserException("id key not found within object", "meow!"); 
    AttenderGroupChange obj = this.repo.getById(id);
    this.objectMapper.updateValue(obj, map);
    return save(obj);
  }
  
  public AttenderGroupChange make(Map<Object, Object> map) throws UserException, JsonMappingException {
    AttenderGroupChange agc = new AttenderGroupChange();
    agc.setDateJoined(LocalDate.parse((String)map.get("dateJoined")));
    agc.setDateLeft(LocalDate.parse((String)map.get("dateLeft")));
    Uczestnik attender = this.serviceAttender.byIdLazy(((Integer)map.get("attenderId")).intValue());
    agc.setAttender(attender);
    Grupa group = this.serviceGroups.byIdLazy(((Integer)map.get("groupId")).intValue());
    agc.setGroup(group);
    return save(agc);
  }
  
  public AttenderGroupChange save(AttenderGroupChange c) {
    return (AttenderGroupChange)this.repo.save(c);
  }
  
  public void delete(AttenderGroupChange c) {
    this.repo.delete(c);
  }
}
