package com.melck.userservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    public static final String ADMIN = "admin";
    public static final String USER = "user";
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/cart/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
