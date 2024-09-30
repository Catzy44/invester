package me.catzy.prestiz.objects.sms_numbers;

import jakarta.transaction.Transactional;
import me.catzy.prestiz.objects.sms.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class SMSNumberService {
  @Autowired
  private SMSNumberRepository repo;
  
  @Autowired
  private SMSService serviceSMS;
  
  @Cacheable(value = {"SMSNumberByNumberStr"}, key = "#number")
  public SMSNumber byNumber(String number) {
    return this.repo.findByNumber(number);
  }
  
  @CacheEvict(value = {"SMSNumberByNumberStr"}, key = "#number.number")
  public SMSNumber save(SMSNumber number) {
    return (SMSNumber)this.repo.save(number);
  }
}
