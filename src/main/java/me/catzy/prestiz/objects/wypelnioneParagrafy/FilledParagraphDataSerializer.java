package me.catzy.prestiz.objects.wypelnioneParagrafy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import me.catzy.prestiz.ToolBox;
import me.catzy.prestiz.objects.grupy.Grupa;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Component;

@Component
@JsonComponent
public class FilledParagraphDataSerializer extends JsonSerializer<FilledParagraphData> {
  public void serialize(FilledParagraphData toSerialize, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
    jgen.writeStartObject();
    jgen.writeStringField("imie", toSerialize.getImie());
    jgen.writeStringField("nazwisko", toSerialize.getNazwisko());
    jgen.writeNumberField("wiek", toSerialize.getWiek());
    jgen.writeNumberField("plec", toSerialize.getPlec());
    jgen.writeStringField("email", toSerialize.getEmail());
    jgen.writeStringField("telefon", toSerialize.getTelefon());
    jgen.writeBooleanField("checked", toSerialize.isChecked());
    if (toSerialize.getCode() != 0)
      jgen.writeNumberField("code", toSerialize.getCode()); 
    if (toSerialize.getGroups() != null) {
      jgen.writeArrayFieldStart("groups");
      for (int i : toSerialize.getGroups())
        jgen.writeNumber(i); 
      jgen.writeEndArray();
    } 
    if (toSerialize.getGroups() != null && (toSerialize.getGroups()).length > 0) {
      jgen.writeArrayFieldStart("groupsEntities");
      Grupa[] byIds = ToolBox.getService().getByIds(toSerialize.getGroups()), groups = byIds;
      for (Grupa g : byIds) {
        ObjectMapper objectMapper = ((JsonMapper.Builder)JsonMapper.builder().disable(new MapperFeature[] { MapperFeature.DEFAULT_VIEW_INCLUSION })).build();
        objectMapper.writerWithView(vGrupaIdAndName.class).writeValue(jgen, g);
      } 
      jgen.writeEndArray();
    } 
    jgen.writeEndObject();
  }
  
  public static interface vGrupaIdAndName extends Grupa.vGrupaId, Grupa.vGrupaNazwa {}
}
