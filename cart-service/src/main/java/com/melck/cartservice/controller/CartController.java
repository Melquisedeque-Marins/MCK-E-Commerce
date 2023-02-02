package com.melck.cartservice.controller;

import com.melck.cartservice.dto.CartRequest;
import com.melck.cartservice.entity.Cart;
import com.melck.cartservice.entity.Product;
import com.melck.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addToCart(@RequestBody CartRequest cartRequest) {
        Cart cart = cartService.addToCart(cartRequest);
        return ResponseEntity.ok().body(cart);
    }

}
