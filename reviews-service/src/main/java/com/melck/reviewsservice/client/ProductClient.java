package com.melck.reviewsservice.client;

import com.melck.reviewsservice.dto.Product;
import com.melck.reviewsservice.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RequiredArgsConstructor
@Component
public class ProductClient {

    private final WebClient webClient;
    @Value("${app-config.client.product}")
    private String productServiceUri;

    public Product getProductInProductService(Long id) {

        try {
            return webClient.get()
                     .uri(buildUri(id))
                     .retrieve()
                     .bodyToMono(Product.class)
                     .block();
        } catch (WebClientResponseException e) {
            throw new ResourceNotFoundException("Product not found");
        }
    }

    private String buildUri(Long id) {
        return String.format(productServiceUri, id.toString());
    }

}
