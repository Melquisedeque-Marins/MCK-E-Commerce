package com.melck.cartservice.controller;

import com.melck.cartservice.dto.CartRequest;
import com.melck.cartservice.entity.Cart;
import com.melck.cartservice.entity.Product;
import com.melck.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Cart cart = cartService.getCartById(id);
        return ResponseEntity.ok().body(cart);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long id, @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.addProductToCart(id, cartRequest);
        return ResponseEntity.ok().body(cart);
    }

}
