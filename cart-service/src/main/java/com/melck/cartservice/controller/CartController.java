package com.melck.cartservice.controller;

import com.melck.cartservice.dto.CartRequest;
import com.melck.cartservice.dto.CartResponse;
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

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Long cartId) {
        CartResponse cartResponse = cartService.getCartById(cartId);
        return ResponseEntity.ok().body(cartResponse);
    }

    @PatchMapping("/add/{cartId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long cartId, @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.addProductToCart(cartId, cartRequest);
        return ResponseEntity.ok().body(cart);
    }

    @PatchMapping("/remove/{cartId}")
    public ResponseEntity<Cart> removeToCart(@PathVariable Long cartId, @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.removeProductToCart(cartId, cartRequest);
        return ResponseEntity.ok().body(cart);
    }

}
