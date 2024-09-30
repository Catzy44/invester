package me.catzy.prestiz.security;

import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.objects.sesje.Sesja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/security"})
public class SecurityController {
  @Autowired
  SecurityService service;
  
  @PostMapping({"/login"})
  public ResponseEntity<Sesja> aktywnyFormularz(@RequestBody PacketInstruktorCreditienals creditienals) throws Exception {
    if (creditienals == null)
      throw new UserException("no request body", "zapytanie nie posiada ciała"); 
    if (creditienals.getNick() == null)
      throw new UserException("no parameter: nick", "brak parametru nick w zapytaniu"); 
    if (creditienals.getPassword() == null)
      throw new UserException("no parameter: password", "brak parametru password w zapytaniu"); 
    return new ResponseEntity(this.service.tryToAuthenticateInstruktor(creditienals.getNick(), creditienals.getPassword()), (HttpStatusCode)HttpStatus.CREATED);
  }
  
  @GetMapping({"/handshake"})
  public void handshake() {}
}
