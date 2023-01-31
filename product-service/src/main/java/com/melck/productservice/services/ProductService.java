package com.melck.productservice.services;

import com.melck.productservice.entity.Product;
import com.melck.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;

    public Product insert (Product product) {
        return repository.save(product);
    }
}
