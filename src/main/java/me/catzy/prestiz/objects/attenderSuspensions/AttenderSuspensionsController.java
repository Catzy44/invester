package me.catzy.prestiz.objects.attenderSuspensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/attenderSuspensions"})
public class AttenderSuspensionsController {
  @Autowired
  AttenderSuspensionsService service;
}
