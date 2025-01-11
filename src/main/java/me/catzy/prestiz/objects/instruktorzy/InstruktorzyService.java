package me.catzy.prestiz.objects.instruktorzy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.catzy.prestiz.RandomString;
import me.catzy.prestiz.exceptions.AuthenticationException;
import me.catzy.prestiz.exceptions.UserException;
import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class InstruktorzyService extends GenericServiceImpl<Instruktor, Long> {
	public InstruktorzyService(InstruktorzyRepository repository) {
		super(repository);
	}
	
  @Autowired private InstruktorzyRepository repo;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  public Instruktor byId(int id) {
    return this.repo.getById(id);
  }
  
  public Instruktor byNick(String nick) {
    return this.repo.getByNick(nick);
  }
  
  public static void main(String args[]) {
	  String x = "zaq1@WSX";
	  
	  try {
		String s = generateAuthmeString(x);
		
		String hash = s;
	    String salt = hash.split("\\$")[2];
	    String newHash = "$SHA$" + salt + "$" + hashSha256(hashSha256(x));
	    if (newHash.equals(hash))
	    	System.out.println("OK");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	    
  }
  
  public static String generateAuthmeString(String password) throws Exception {
		RandomString rs = new RandomString(16);
		
		String salt = rs.nextString();
		String doubleHashedPassword = hashSha256(hashSha256(password) + salt);
		return "$SHA$" + salt + "$" + doubleHashedPassword;
	}
  
  public void authenticate(Instruktor instruktor, String providedPassword) throws Exception {
    if (instruktor == null)
      throw new AuthenticationException("user not found"); 
    if (providedPassword == null)
      throw new AuthenticationException("password is null"); 
    String hash = instruktor.getPassword();
    String salt = hash.split("\\$")[2];
    String newHash = "$SHA$" + salt + "$" + hashSha256(hashSha256(providedPassword));
    if (newHash.equals(hash))
      return; 
    throw new AuthenticationException("password does not match");
  }
  
  public static String hashSha256(String tohash) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] encodedhash = digest.digest(tohash.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(encodedhash);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xFF & hash[i]);
      if (hex.length() == 1)
        hexString.append('0'); 
      hexString.append(hex);
    } 
    return hexString.toString();
  }
  
  public List<Instruktor> getAll() {
    return this.repo.findAll();
  }
  
  public Instruktor save(Instruktor s) {
    return (Instruktor)this.repo.save(s);
  }
  
  public Instruktor patch(int id, Map<Object, Object> map) throws UserException, JsonMappingException {
    Instruktor obj = this.repo.getById(id);
    this.objectMapper.updateValue(obj, map);
    return save(obj);
  }
}
