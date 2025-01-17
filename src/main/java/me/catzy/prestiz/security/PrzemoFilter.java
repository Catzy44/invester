package me.catzy.prestiz.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.catzy.prestiz.objects.sesje.Sesja;
import me.catzy.prestiz.objects.sesje.SesjeService;

@Component
public class PrzemoFilter extends OncePerRequestFilter {
  private static final String HEADER_STRING = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";
  
  @Autowired
  SesjeService sesjeService;
  
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader(HEADER_STRING);
    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter((ServletRequest)req, (ServletResponse)res);
      return;
    } 
    String token = header.replace(TOKEN_PREFIX, "");
    try {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication((Authentication)authentication);
      chain.doFilter((ServletRequest)req, (ServletResponse)res);
    } catch (Exception e) {
      res.setStatus(401);
      res.getWriter().write(e.getMessage());
      res.getWriter().flush();
      e.printStackTrace();
    } 
  }
  
  private UsernamePasswordAuthenticationToken getAuthentication(String token) throws Exception {
      Sesja session = this.sesjeService.findActiveSessionByKey(token);
      
      if(session == null) {
    	  throw new Exception("SESSION INVALID");
      }
      
      List<GrantedAuthority> ewqr = new ArrayList<>();
      ewqr.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      UsernamePasswordAuthenticationToken tokeasdfn = new UsernamePasswordAuthenticationToken(session.getInstruktor(), null, ewqr);
      return tokeasdfn;
  }
}
