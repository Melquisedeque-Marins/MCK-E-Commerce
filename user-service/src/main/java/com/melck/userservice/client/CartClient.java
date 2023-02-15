package com.melck.userservice.client;

import com.melck.userservice.dto.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class CartClient {

    private final WebClient webClient;
    @Value("${app-config.client.cart}")
    private String cartServiceUri;

    public Long getCartId () {
        try {
            log.info("creating cart in cart service");
            Cart cart = webClient.post()
                    .uri(cartServiceUri)
                    .retrieve()
                    .bodyToMono(Cart.class)
                    .block();
            return cart.getId();
        } catch (Exception e) {
            log.error("error when creating a new cart in a service cart" + e.getMessage(), e);
            throw e;
        }

    }


}
