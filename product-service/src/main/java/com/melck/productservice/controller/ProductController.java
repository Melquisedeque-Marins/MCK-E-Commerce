package com.melck.productservice.controller;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.entity.Product;
import com.melck.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService service;
    @PostMapping
    public ResponseEntity<ProductResponse> insert(@RequestBody ProductRequest productRequest) {
        ProductResponse newProduct = service.insert(productRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newProduct.getId()).toUri();
        return ResponseEntity.created(uri).body(newProduct);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = service.getAllProduct();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/cart")
    public ResponseEntity<List<ProductResponse>> getProductsIntoACart(@RequestParam Set<Long> productsId) {
        List<ProductResponse> products = service.getProductsInACart(productsId);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = service.getProductById(id);
        return ResponseEntity.ok().body(product);
    }
}
