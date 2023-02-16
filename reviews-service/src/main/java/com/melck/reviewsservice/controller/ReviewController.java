package com.melck.reviewsservice.controller;

import com.melck.reviewsservice.dto.ReviewRequest;
import com.melck.reviewsservice.dto.ReviewResponse;
import com.melck.reviewsservice.service.ReviewService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/product/{Id}")
    @CircuitBreaker(name = "user",  fallbackMethod = "fallbackMethod")
    public ResponseEntity<ReviewResponse> newReview(@Valid @RequestBody ReviewRequest reviewRequest, @PathVariable Long Id) {
        ReviewResponse newReview = reviewService.newReview(reviewRequest, Id);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newReview.getId()).toUri();
        return ResponseEntity.created(uri).body(newReview);
    }

    @GetMapping("/{productId}")
    @CircuitBreaker(name = "user",  fallbackMethod = "userFallbackMethod")
    public ResponseEntity<List<ReviewResponse>> getAllReviewByProduct(@PathVariable Long productId) {
        List<ReviewResponse> reviews = reviewService.getAllReviewByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    public ResponseEntity<String> fallbackMethod(Exception e) {
        log.info("Oops! Something went wrong, the user service or product service is down. Please try again later.", e);
        return ResponseEntity.ok().body("Oops! Something went wrong, Please try again later.");
    }

    public ResponseEntity<String> userFallbackMethod(Exception e) {
        log.info("Oops! Something went wrong, the user service is down. Please try again later.", e);
        return ResponseEntity.ok().body("Oops! Something went wrong, Please try again later.");
    }
}
