package me.catzy.prestiz.objects.sms_codes;

import me.catzy.prestiz.objects.gcaptcha.GCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/sms"})
public class SMSCodeController {
  @Autowired
  SMSCodeService service;
  
  @Autowired
  GCaptchaService serviceGCaptcha;
}
