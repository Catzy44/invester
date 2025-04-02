package me.catzy.invester;

import java.io.File;
import java.io.FileWriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.rest.core.config.ProjectionDefinitionConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.projection.ProjectionDefinitions;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableScheduling
@EnableCaching
public class InvesterApplication {
	public static final boolean SMS_CACHE_PRELOAD_DISABLED = false;

	public static void main(String[] args) {
		try {
			long pid = ProcessHandle.current().pid();
			File pidF = new File("proc.pid");
			FileWriter fw = new FileWriter(pidF);
			fw.write("" + pid);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SpringApplication application = new SpringApplication(new Class[] { InvesterApplication.class });
		application.run(args);
	}

	public static void setCorsRegistry(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins(new String[] { "*" })
		.allowedMethods(new String[] { "GET", "PUT", "DELETE", "POST", "PATCH", "OPTIONS" })
		.allowCredentials(false)
		.maxAge(3600L);
	}
	
	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return new RepositoryRestConfigurer() {
			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
				InvesterApplication.setCorsRegistry(cors);
			}
		};
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {//
			public void addCorsMappings(CorsRegistry registry) {
				InvesterApplication.setCorsRegistry(registry);
			}
		};
	}
	
	@Bean
	public CacheManager cacheManager() {
		return (CacheManager) new ConcurrentMapCacheManager(new String[] { "test" });
	}

	@Bean
	public ProjectionFactory projectionFactory() {
		return (ProjectionFactory) new SpelAwareProxyProjectionFactory();
	}

	@Bean
	public ProjectionDefinitions projectionDefinitions() {
		return (ProjectionDefinitions) new ProjectionDefinitionConfiguration();
	}
}
