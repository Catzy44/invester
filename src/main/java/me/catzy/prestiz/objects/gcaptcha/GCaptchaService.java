package me.catzy.prestiz.objects.gcaptcha;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Transactional
@Service
public class GCaptchaService {
  public void checkCaptcha(String captcha, String remoteaddr) throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://www.google.com/recaptcha/api/siteverify").queryParam("secret", new Object[] { "6LcnUpYnAAAAAIj8JqtDSuiQYbz09miuV6Pm0ElO" }).queryParam("response", new Object[] { captcha }).queryParam("remoteip", new Object[] { remoteaddr });
    HttpEntity<?> entity = new HttpEntity((MultiValueMap)headers);
    ResponseEntity responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, GCaptchaResponse.class, new Object[0]);
    if (!responseEntity.hasBody())
      throw new Exception("no body returned from gcaptcha"); 
    if (!((GCaptchaResponse)responseEntity.getBody()).isSuccess())
      throw new Exception("unsuccessfull captcha"); 
  }
}
