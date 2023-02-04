package com.melck.reviewsservice.controller;

import com.melck.reviewsservice.entity.Review;
import com.melck.reviewsservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> newReview(@RequestBody Review review) {
        Review newReview = reviewService.newReview(review);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newReview.getId()).toUri();
        return ResponseEntity.created(uri).body(newReview);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<Review>> getAllReviewByProduct(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getAllReviewByProduct(productId);
        return ResponseEntity.ok(reviews);
    }
}
