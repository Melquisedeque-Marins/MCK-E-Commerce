package com.melck.orderservice.controller;

import com.melck.orderservice.dto.OrderRequest;
import com.melck.orderservice.entity.Order;
import com.melck.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{cartId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long cartId) {
        Order order = orderService.placeOrder(cartId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order);
    }
}
