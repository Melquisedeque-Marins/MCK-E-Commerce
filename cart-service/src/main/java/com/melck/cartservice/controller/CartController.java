package com.melck.cartservice.controller;

import com.melck.cartservice.dto.CartRequest;
import com.melck.cartservice.dto.CartResponse;
import com.melck.cartservice.entity.Cart;
import com.melck.cartservice.service.CartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> newCart() {
        Cart cart = cartService.newCart();
        return ResponseEntity.ok().body(cart);
    }

    @GetMapping("/{cartId}")
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackMethod")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Long cartId) {
        CartResponse cartResponse = cartService.getCartById(cartId);
        return ResponseEntity.ok().body(cartResponse);
    }

    @PatchMapping("/add/{cartId}")
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackMethod")
    public ResponseEntity<Cart> addToCart(@PathVariable Long cartId, @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.addProductToCart(cartId, cartRequest);
        return ResponseEntity.ok().body(cart);
    }

    @PatchMapping("/remove/{cartId}")
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackMethod")
    public ResponseEntity<Cart> removeToCart(@PathVariable Long cartId, @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.removeProductToCart(cartId, cartRequest);
        return ResponseEntity.ok().body(cart);
    }

    @PatchMapping("/empty-the-cart/{cartId}")
    public ResponseEntity<Cart> emptyTheCart(@PathVariable Long cartId) {
        Cart cart = cartService.emptyTheCart(cartId);
        return ResponseEntity.ok().body(cart);
    }

    public ResponseEntity<String> fallbackMethod(WebClientResponseException e) {
        log.info("Oops! Something went wrong, the product service is down. Please try again later.", e);
        return ResponseEntity.ok().body("Oops! Something went wrong, the product service is down. Please try again later.");
    }
}
