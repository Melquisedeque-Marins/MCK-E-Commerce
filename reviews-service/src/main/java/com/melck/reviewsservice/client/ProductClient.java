package com.melck.reviewsservice.client;

import com.melck.reviewsservice.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductClient {
    private final WebClient webClient;
    @Value("${app-config.client.product}")
    private String productServiceUri;

    public ProductResponse getProductInProductService(Long id) {
        try {
            return webClient.get()
                     .uri(buildUri(id))
                     .retrieve()
                     .bodyToMono(ProductResponse.class)
                     .block();
        } catch (Exception e) {
            log.error("error when Searching a user in a product service " + e.getMessage(), e);
            throw e;
        }
    }

    public void updateRateInProductService (ProductResponse product) {
        try {
             webClient.patch()
                    .uri(buildUri(product.getId()))
                    .body(Mono.just(product), ProductResponse.class)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("error when update the product in a product service " + e.getMessage(), e);
            throw e;
        }
    }

    private String buildUri(Long id) {
        return String.format(productServiceUri, id.toString());
    }

}
