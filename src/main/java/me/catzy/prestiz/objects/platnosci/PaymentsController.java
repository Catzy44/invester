package me.catzy.prestiz.objects.platnosci;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import me.catzy.prestiz.objects.grupy.GrupyService;
import me.catzy.prestiz.objects.miejsca.MiejscaService;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.sms.SMSService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@RestController
@RequestMapping({"/payments"})
public class PaymentsController {
  @Autowired
  PaymentsService service;
  
  @Autowired
  SMSService serviceSMS;
  
  @Autowired
  MiejscaService serviceMiejsca;
  
  @Autowired
  GrupyService serviceGrupy;
  
  @JsonView({getUczestnik.class})
  @GetMapping({"/{id}"})
  public Payment getUczestnik(@PathVariable int id) {
    return this.service.byId(id);
  }
  
  private static interface getUczestnikWithMiejsce extends 
  Uczestnik.vUczestnikFull, Uczestnik.vUczestnikMiejsce, Uczestnik.vUczestnikSeason, 
  Miejsce.vMiejsceId, Miejsce.vMiejsceNazwa, 
  Season.vSeasonId, Season.vSeasonName {}
  
  @JsonView({getUczestnikWithMiejsce.class})
  @GetMapping({"/{id}/with_miejsce"})
  public Payment getUczestnikWithMiejsce(@PathVariable int id) {
    return this.service.byId(id);
  }
  private static interface getUczestnik extends Uczestnik.vUczestnikFull {}
  
  }
