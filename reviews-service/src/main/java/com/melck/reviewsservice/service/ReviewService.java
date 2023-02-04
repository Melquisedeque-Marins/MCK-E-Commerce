package com.melck.reviewsservice.service;

import com.melck.reviewsservice.entity.Review;
import com.melck.reviewsservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review newReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviewByProduct(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviews;
    }
}
