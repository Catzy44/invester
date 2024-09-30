package me.catzy.prestiz.security;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurity {
  private final PrzemoFilter filter;
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authz -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)authz.requestMatchers(HttpMethod.OPTIONS)).permitAll().requestMatchers(new String[] { "/security/handshake" })).authenticated().requestMatchers(new String[] { "/security/**" })).permitAll().requestMatchers(new String[] { "/formularze/active" })).permitAll().requestMatchers(new String[] { "/formularze/p_**" })).permitAll().requestMatchers(new String[] { "/sms/p_**" })).permitAll().requestMatchers(new String[] { "/formularze/*/p_**" })).permitAll().anyRequest()).authenticated()).addFilterBefore((Filter)this.filter, UsernamePasswordAuthenticationFilter.class);
    return (SecurityFilterChain)http.build();
  }
  
  public WebSecurity(PrzemoFilter filter) {
    this.filter = filter;
  }
}
