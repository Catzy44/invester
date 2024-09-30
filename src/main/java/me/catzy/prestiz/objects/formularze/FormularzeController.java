package me.catzy.prestiz.objects.formularze;

import com.fasterxml.jackson.annotation.JsonView;
import me.catzy.prestiz.objects.wypelnioneParagrafy.WypelnionyParagraf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/formularze"})
public class FormularzeController {
  @Autowired
  FormularzeService service;
  
  @JsonView({Pparagrafy.class})
  @GetMapping({"/{id}/p_paragrafy"})
  public ResponseEntity<Object> Pparagrafy(@PathVariable("id") int id) throws Exception {
    Formularz form = this.service.getById(id);
    if (!form.isActive())
      throw new Exception("formularz is not active"); 
    return new ResponseEntity(this.service.getParagraphListWithAdditionalData(form), (HttpStatusCode)HttpStatus.ACCEPTED);
  }
  
  @JsonView({aktywneFormularze.class})
  @GetMapping({"/p_active"})
  public ResponseEntity<Object> aktywneFormularze() {
    return new ResponseEntity(this.service.buildActiveFormularz(), (HttpStatusCode)HttpStatus.ACCEPTED);
  }
  
  @JsonView({zapiszFormularz.class})
  @PostMapping({"/p_submit"})
  public ResponseEntity<Object> zapiszFormularz(@RequestBody WypelnionyParagraf[] paragrafy) throws Exception {
    return new ResponseEntity(this.service.submitForm(paragrafy), (HttpStatusCode)HttpStatus.ACCEPTED);
  }
  
  private static interface Pparagrafy {}
  
  private static interface aktywneFormularze {}
  
  private static interface zapiszFormularz {}
}
