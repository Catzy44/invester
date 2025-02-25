package me.catzy.invester.configuration;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import me.catzy.invester.InvesterApplication;

@Component
public class SpringDataRestConfig implements RepositoryRestConfigurer {
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
    InvesterApplication.setCorsRegistry(cors);
  }
}
