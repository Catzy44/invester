package me.catzy.prestiz.objects.wypelnioneParagrafy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Arrays;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.grupy.GrupyService;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupArraySerializer extends JsonSerializer<int[]> {
  @Autowired
  GrupyService serviceGrupy;
  
  public void serialize(int[] groups, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    Grupa[] groupEntities = this.serviceGrupy.getByIds(groups);
    jsonGenerator.writeObject(Arrays.asList(groupEntities));
  }
}
