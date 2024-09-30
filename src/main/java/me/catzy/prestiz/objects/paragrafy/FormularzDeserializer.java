package me.catzy.prestiz.objects.paragrafy;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import me.catzy.prestiz.objects.formularze.Formularz;
import me.catzy.prestiz.objects.formularze.FormularzeService;
import org.springframework.beans.factory.annotation.Autowired;

public class FormularzDeserializer extends JsonDeserializer<Formularz> {
  @Autowired
  private FormularzeService formularzService;
  
  public Formularz deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    int paragrafId = jsonParser.getValueAsInt();
    return this.formularzService.byIdLazy(paragrafId);
  }
}
