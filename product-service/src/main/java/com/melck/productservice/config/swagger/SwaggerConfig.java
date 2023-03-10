package com.melck.productservice.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

public class SwaggerConfig {


    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("product-service")
                .packagesToScan("com.melck.productservice.controller")
                .pathsToMatch("/api/v1/products/**")
//                .addOpenApiCustomizer(bearerAuthCustomizer())
                .build();
    }

    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service API")
                        .description("Micro service to management product features")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MCK Enterprises")
                                .url("https://github.com/Melquisedeque-Marins")
                                .email("melck_junior@hotmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    private OpenApiCustomizer customOpenId() {
        return openApi -> openApi
                .schemaRequirement("Bearer", new SecurityScheme()
                        .name("Authorization")
                        .description("JWT Authorization header using the Bearer scheme. Example: Authorization: Bearer {token}")
                        .type(SecurityScheme.Type.OPENIDCONNECT)
                        .in(SecurityScheme.In.HEADER))
                .security(Collections.singletonList(new SecurityRequirement().addList("Bearer")));
    }

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("spring_oauth", new SecurityScheme()
//                                .type(SecurityScheme.Type.OAUTH2)
//                                .description("Oauth2 flow")
//                                .flows(new OAuthFlows()
//                                        .clientCredentials(new OAuthFlow()
//                                                .tokenUrl("http://localhost:8080" + "/oauth/token")
//                                                .scopes(new Scopes()
//                                                        .addString("read", "for read operations")
//                                                        .addString("write", "for write operations")
//                                                ))))
//                )
//                .security(Arrays.asList(
//                        new SecurityRequirement().addList("spring_oauth")))
//                .info(new Info()
//                        .title("Book Application API")
//                        .description("This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")
//                        .termsOfService("terms")
//                        .contact(new Contact().email("codersitedev@gmail.com").name("Developer: Moises Gamio"))
//                        .license(new License().name("GNU"))
//                        .version("2.0")
//                );
//    }

}