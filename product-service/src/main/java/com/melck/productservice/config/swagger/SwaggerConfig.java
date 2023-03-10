package com.melck.productservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("product-service")
                .pathsToMatch("/api/v1/products/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Product Service API")
                        .description("Micro service to management product features")
                        .version("V1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }

//    @Bean
//    public Docket getDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.melck.doctor.ms"))
//                .build()
//                .apiInfo(metaData());
//
//    }
//
//    private ApiInfo metaData() {
//        return new ApiInfoBuilder()
//                .title("Doctor Label API")
//                .description("Spring Boot REST API for labelling of medical cases")
//                .version("1.0.0")
//                .license("Apache License Version 2.0")
//                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
//                .contact(contact())
//                .build();
//    }
//
//    private Contact contact () {
//        return new Contact("Melquisedeque Marins"
//                , "https://github.com/Melquisedeque-Marins"
//                , "melck_junior@hotmail.com");
//    }
}
