package com.melck.cartservice.client;


import com.melck.cartservice.dto.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductClient {
    private final WebClient webClient;
    @Value("${app-config.client.product}")
    private String productServiceUri;
    @Value("${app-config.client.product-cart}")
    private String productServiceCartUri;

    public Product getProductInProductService(Long id) {
        try {
            return webClient.get()
                    .uri(buildUri(id))
                    .retrieve()
                    .bodyToMono(Product.class)
                    .block();
        } catch (Exception e) {
            log.error("error when Searching a user in a product service " + e.getMessage(), e);
            throw e;
        }
    }

    public Product[] getProductInACartProductService(Set<Long> productsId) {
        return webClient.get()
                .uri(productServiceCartUri,
                        uriBuilder -> uriBuilder.queryParam("productsId", productsId).build())
                .retrieve()
                .bodyToMono(Product[].class)
                .block();
    }

    private String buildUri(Long id) {
        return String.format(productServiceUri, id.toString());
    }

}

