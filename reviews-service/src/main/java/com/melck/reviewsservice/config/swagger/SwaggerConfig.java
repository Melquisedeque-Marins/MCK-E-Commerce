package com.melck.reviewsservice.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;

import java.util.Collections;

public class SwaggerConfig {


    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("product-service")
                .packagesToScan("com.melck.reviewsservice.controller")
                .pathsToMatch("/api/v1/reviews/**")
//                .addOpenApiCustomizer(bearerAuthCustomizer())
                .build();
    }

    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Review Service API")
                        .description("Micro service to management reviews features")
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

}