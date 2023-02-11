package com.melck.productservice.controller;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.entity.Product;
import com.melck.productservice.services.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService service;
    @PostMapping
    public ResponseEntity<ProductResponse> insert(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse newProduct = service.insert(productRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newProduct.getId()).toUri();
        return ResponseEntity.created(uri).body(newProduct);
    }

    @GetMapping()
    @CircuitBreaker(name = "review", fallbackMethod = "fallbackMethod")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = service.getAllProduct();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "review", fallbackMethod = "fallbackMethod")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = service.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    public ResponseEntity fallbackMethod(Long id, Exception e) {
        return ResponseEntity.ok().body("Oops! Something went wrong, please try again later");
    }


    @GetMapping("/cart")
    public ResponseEntity<List<ProductResponse>> getProductsIntoACart(@RequestParam Set<Long> productsId) {
        List<ProductResponse> products = service.getProductsInACart(productsId);
        return ResponseEntity.ok().body(products);
    }


}
