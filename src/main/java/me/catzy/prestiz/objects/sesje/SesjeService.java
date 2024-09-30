package me.catzy.prestiz.objects.sesje;

import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Random;
import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.security.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class SesjeService {
  @Autowired
  private SesjeRepository repo;
  
  private Random r = new Random();
  
  private RandomString rs = new RandomString(32, this.r, RandomString.alphanum);
  
  public Sesja byId(int id) {
    return this.repo.getById(id);
  }
  
  @Cacheable(value = {"sesje"}, key = "#key", condition = "#key != null")
  public Sesja findActiveSessionByKey(String key) throws Exception {
    Sesja s = this.repo.getActiveSessionByKey(key);
    if (s == null)
      throw new Exception("cannot find session"); 
    if (isSessionExpired(s)) {
      System.out.println("[SesjeService] Session for user " + s.getInstruktor().getNick() + " expired!");
      killSession(s);
      return null;
    } 
    return s;
  }
  
  public boolean isSessionExpired(Sesja s) {
    if (s.getInstruktor().getNick().equals("sms1"))
      return false; 
    if (System.currentTimeMillis() - s.getLast_login_date().getTime() > 1209600000L)
      return true; 
    BigInteger milispassed = new BigInteger("" + System.currentTimeMillis());
    milispassed.subtract(new BigInteger("" + s.getFirst_login_date().getTime()));
    BigInteger aMonth = new BigInteger("86400000");
    aMonth.multiply(new BigInteger("30"));
    return (milispassed.subtract(aMonth).longValue() < 0L || !s.isActive());
  }
  
  @CacheEvict(value = {"sesje"}, key = "#s.session_id", condition = "#s != null")
  public void killSession(Sesja s) {
    s.setActive(false);
    save(s);
  }
  
  public Sesja createNewSession(Instruktor instruktor) {
    Sesja s = new Sesja();
    Timestamp now = new Timestamp(System.currentTimeMillis());
    s.setFirst_login_date(now);
    s.setLast_login_date(now);
    s.setInstruktor(instruktor);
    s.setActive(true);
    s.setSession_id(this.rs.nextString());
    return save(s);
  }
  
  public Sesja save(Sesja s) {
    return (Sesja)this.repo.save(s);
  }
}
