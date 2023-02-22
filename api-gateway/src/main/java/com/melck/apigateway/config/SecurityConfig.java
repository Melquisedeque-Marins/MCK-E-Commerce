package com.melck.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {

        httpSecurity
                .csrf().disable()
                .authorizeExchange((exchange) -> exchange
                        .pathMatchers("/eureka/**").permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        httpSecurity
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/v1/products/**")
                .permitAll();
        return httpSecurity.build();
    }
}
