package com.mateusz.rest.webservices.restful_web_services.security;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Lambda DSL w/ "authz" meaning authorization
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated() // 1. All requests should be authenticated
                )
                .httpBasic(withDefaults()) // 2. If a request is not authenticated, a web page is shown
                .csrf(csrf -> csrf
                        .disable() // 3. Disable CSRF (impacts POST & PUT routes)
                );

        return http.build();

    }
}
