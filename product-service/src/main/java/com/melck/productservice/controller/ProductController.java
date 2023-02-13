package com.melck.productservice.controller;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.entity.Product;
import com.melck.productservice.services.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
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
    @Cacheable(value = "products")
    @CircuitBreaker(name = "review", fallbackMethod = "fallbackMethod")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Searching into review service");
        List<ProductResponse> products = service.getAllProduct();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "product", key = "#id")
    @CircuitBreaker(name = "review", fallbackMethod = "fallbackMethod2")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = service.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    public ResponseEntity fallbackMethod(WebClientResponseException e) {
        log.info("Oops! Something went wrong, the review service is down. Please try again later.", e);
        return ResponseEntity.ok().body("Oops! Something went wrong, the review service is down. Please try again later.");
    }

    public ResponseEntity fallbackMethod2(Long id, WebClientResponseException e) {
        return ResponseEntity.ok().body("Oops! Something went wrong, the review service is down. Please try again later.");
    }


    @GetMapping("/cart")
    public ResponseEntity<List<ProductResponse>> getProductsIntoACart(@RequestParam Set<Long> productsId) {
        List<ProductResponse> products = service.getProductsInACart(productsId);
        return ResponseEntity.ok().body(products);
    }


}
