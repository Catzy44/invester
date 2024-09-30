package me.catzy.prestiz.objects.sms_templates;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import me.catzy.prestiz.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SMSTemplateService {
  @Autowired
  private SMSTemplateRepository repo;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  public SMSTemplate save(SMSTemplate s) {
    return (SMSTemplate)this.repo.save(s);
  }
  
  public SMSTemplate byId(int id) {
    return this.repo.getById(id);
  }
  
  public SMSTemplate patch(Map<Object, Object> map) throws UserException, JsonMappingException {
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
    SMSTemplate obj = this.repo.getById(id);
    this.objectMapper.updateValue(obj, map);
    return save(obj);
  }
}
