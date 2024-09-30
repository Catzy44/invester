package me.catzy.prestiz.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.catzy.prestiz.exceptions.ExceptionHandler;
import me.catzy.prestiz.objects.sesje.Sesja;
import me.catzy.prestiz.objects.sesje.SesjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class PrzemoFilter extends OncePerRequestFilter {
  private static final String HEADER_STRING = "Authorization";
  
  private static final String TOKEN_PREFIX = "Bearer ";
  
  @Autowired
  SesjeService sesjeService;
  
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter((ServletRequest)req, (ServletResponse)res);
      return;
    } 
    try {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
      SecurityContextHolder.getContext().setAuthentication((Authentication)authentication);
      chain.doFilter((ServletRequest)req, (ServletResponse)res);
    } catch (Exception e) {
      res.setStatus(401);
      e.printStackTrace();
    } 
  }
  
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws Exception {
    String token = request.getHeader("Authorization");
    if (token == null)
      return null; 
    String uid = token.replace("Bearer ", "");
    Sesja sesja = null;
    try {
      sesja = this.sesjeService.findActiveSessionByKey(uid);
    } catch (Exception e) {
      ExceptionHandler.logStacktrace(e);
      throw e;
    } 
    if (sesja != null) {
      List<GrantedAuthority> ewqr = new ArrayList<>();
      ewqr.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      UsernamePasswordAuthenticationToken tokeasdfn = new UsernamePasswordAuthenticationToken(sesja.getInstruktor(), null, ewqr);
      return tokeasdfn;
    } 
    return null;
  }
}
