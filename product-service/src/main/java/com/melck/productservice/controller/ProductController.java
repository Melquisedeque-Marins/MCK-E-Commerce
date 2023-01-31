package com.melck.productservice.controller;

import com.melck.productservice.entity.Product;
import com.melck.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    
    private final ProductService service;
    @PostMapping
    public ResponseEntity<Product> insert(@RequestBody Product product) {
        Product newProduct = service.insert(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newProduct.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }
}
