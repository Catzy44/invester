package me.catzy.prestiz.security;

import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.instruktorzy.InstruktorzyService;
import me.catzy.prestiz.objects.sesje.Sesja;
import me.catzy.prestiz.objects.sesje.SesjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecurityService {
  @Autowired
  SesjeService sesjeService;
  
  @Autowired
  InstruktorzyService instruktorzyService;
  
  public Sesja tryToAuthenticateInstruktor(String login, String password) throws Exception {
    Instruktor instruktor = this.instruktorzyService.byNick(login);
    this.instruktorzyService.authenticate(instruktor, password);
    return this.sesjeService.createNewSession(instruktor);
  }
}
