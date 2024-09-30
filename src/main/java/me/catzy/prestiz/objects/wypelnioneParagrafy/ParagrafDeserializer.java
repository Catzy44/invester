package me.catzy.prestiz.objects.wypelnioneParagrafy;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import me.catzy.prestiz.objects.paragrafy.Paragraf;
import me.catzy.prestiz.objects.paragrafy.ParagrafyService;
import org.springframework.beans.factory.annotation.Autowired;

public class ParagrafDeserializer extends JsonDeserializer<Paragraf> {
  @Autowired
  private ParagrafyService paragrafService;
  
  public Paragraf deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    int paragrafId = jsonParser.getValueAsInt();
    return this.paragrafService.byIdLazy(paragrafId);
  }
}
