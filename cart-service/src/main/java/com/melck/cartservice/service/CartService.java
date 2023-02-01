package com.melck.cartservice.service;

import com.melck.cartservice.entity.Product;
import com.melck.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository repository;

    public List<Product> addToCart(List<Long> productIds ) {
    return null;
    }
}
