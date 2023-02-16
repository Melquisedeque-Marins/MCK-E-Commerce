package com.melck.reviewsservice.service;

import com.melck.reviewsservice.client.ProductClient;
import com.melck.reviewsservice.client.UserClient;
import com.melck.reviewsservice.dto.Product;
import com.melck.reviewsservice.dto.ReviewRequest;
import com.melck.reviewsservice.dto.ReviewResponse;
import com.melck.reviewsservice.dto.User;
import com.melck.reviewsservice.entity.Review;
import com.melck.reviewsservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Transactional
    public ReviewResponse newReview(ReviewRequest reviewRequest, Long productId) {
        Product product = productClient.getProductInProductService(productId);
        Review review = mapReviewRequestToReview(reviewRequest);
        review.setProductId(product.getId());
        log.info("Saving review for product with id: {}", productId);
        reviewRepository.save(review);
        return mapReviewToReviewResponse(review);
    }

    public List<ReviewResponse> getAllReviewByProduct(Long productId) {
        log.info("Searching all reviews to product with id: {}", productId);
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        log.info("Returning all reviews to product with id: {}", productId, reviews);
        return reviews.stream()
                .map(this::mapReviewToReviewResponse).toList();
    }

    private Review mapReviewRequestToReview(ReviewRequest reviewRequest) {
        return Review.builder()
                .userId(reviewRequest.getUserId())
                .title(reviewRequest.getTitle())
                .description(reviewRequest.getDescription())
                .rate(reviewRequest.getRate())
                .build();
    }
    private ReviewResponse mapReviewToReviewResponse(Review review) {
        User user = userClient.getUserInUserService(review.getUserId());
        String[] name = user.getFullName().split(" ");
        return ReviewResponse.builder()
                .id(review.getId())
                .userName(name[0])
                .title(review.getTitle())
                .description(review.getDescription())
                .rate(review.getRate())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
