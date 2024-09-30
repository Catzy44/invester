package me.catzy.prestiz.configuration;

import me.catzy.prestiz.PrestizApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class SpringDataRestConfig implements RepositoryRestConfigurer {
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
    PrestizApplication.setCorsRegistry(cors);
  }
}
