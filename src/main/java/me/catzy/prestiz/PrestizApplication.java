package me.catzy.prestiz;

import java.io.File;
import java.io.FileWriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.rest.core.config.ProjectionDefinitionConfiguration;
import org.springframework.data.rest.core.projection.ProjectionDefinitions;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class PrestizApplication {
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
		SpringApplication application = new SpringApplication(new Class[] { PrestizApplication.class });
		application.run(args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				PrestizApplication.setCorsRegistry(registry);
			}
		};
	}

	public static void setCorsRegistry(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins(new String[] { "*" })
		.allowedMethods(new String[] { "GET", "PUT", "DELETE", "POST", "PATCH", "OPTIONS" })
		.allowCredentials(false)
		.maxAge(3600L);
	}

	@Configuration
	public class ProjectionFactoryConfig {
		@Bean
		public ProjectionFactory projectionFactory() {
			return (ProjectionFactory) new SpelAwareProxyProjectionFactory();
		}
	}

	@Configuration
	public class ProjectionDefinitionsConfig {
		@Bean
		public ProjectionDefinitions projectionDefinitions() {
			return (ProjectionDefinitions) new ProjectionDefinitionConfiguration();
		}
	}
}
