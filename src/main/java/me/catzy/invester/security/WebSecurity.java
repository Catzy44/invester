package me.catzy.invester.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurity {

    private final SecurityFilter filter;

    public WebSecurity(SecurityFilter filter) {
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeHttpRequests(auth -> auth
                // pre-flight CORS
                .requestMatchers(HttpMethod.OPTIONS).permitAll()

                // generowanie sesji
                .requestMatchers("/security/**").permitAll()

                // wszystko inne wymaga auth
                .anyRequest().authenticated()
        );

        // własny filtr JWT / tokenów przed UsernamePasswordAuthenticationFilter
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
