package com.melck.productservice.controller;

import com.melck.productservice.dto.ProductRequest;
import com.melck.productservice.dto.ProductResponse;
import com.melck.productservice.entity.Product;
import com.melck.productservice.services.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    @PostMapping
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<ProductResponse> registerProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse newProduct = service.registerProduct(productRequest);
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

    public ResponseEntity<String> fallbackMethod(WebClientResponseException e) {
        log.info("Oops! Something went wrong, the review service is down. Please try again later.", e);
        return ResponseEntity.ok().body("Oops! Something went wrong, the review service is down. Please try again later.");
    }

    @GetMapping("/cart")
    public ResponseEntity<List<ProductResponse>> getProductsIntoACart(@RequestParam Set<Long> productsId) {
        List<ProductResponse> products = service.getProductsInACart(productsId);
        return ResponseEntity.ok().body(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> editProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok().body(service.editProduct(productId, productRequest));
    }

}
