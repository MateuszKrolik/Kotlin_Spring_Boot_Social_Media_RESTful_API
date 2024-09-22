package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.Customizer.withDefaults

@Configuration
class SpringSecurityConfiguration {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http // Lambda DSL w/ "authz" meaning authorization
            .authorizeHttpRequests { authz ->
                authz // enable access to root url for all users for possible Cloud HealthCheck
                    .requestMatchers("/").permitAll()
                    .anyRequest().authenticated()
            } // 1. All requests should be authenticated

            .httpBasic(withDefaults()) // 2. If a request is not authenticated, a web page is shown
            .csrf { csrf ->
                csrf
                    .disable()
            } // 3. Disable CSRF (impacts POST & PUT routes)


        return http.build()
    }
}
