package com.melck.apigateway.config;

import jakarta.ws.rs.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String ADMIN = "admin";
    public static final String USER = "user";


    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.csrf()
                .disable()
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**").permitAll()
//                        .pathMatchers("/api/v1/products/**").hasRole(ADMIN)
                        .pathMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
//                        .pathMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole(ADMIN, USER)
                        .pathMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/users").hasRole(ADMIN)
                        .pathMatchers(HttpMethod.POST, "/api/v1/cart").permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return serverHttpSecurity.build();

    }
}
