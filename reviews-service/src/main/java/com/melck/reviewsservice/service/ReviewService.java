package com.melck.reviewsservice.service;

import com.melck.reviewsservice.dto.Product;
import com.melck.reviewsservice.dto.ReviewRequest;
import com.melck.reviewsservice.dto.ReviewResponse;
import com.melck.reviewsservice.dto.User;
import com.melck.reviewsservice.entity.Review;
import com.melck.reviewsservice.repository.ReviewRepository;
import com.melck.reviewsservice.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final WebClient webClient;

    public ReviewResponse newReview(ReviewRequest reviewRequest, Long productId) {

        try {
            Product product = webClient.get()
                    .uri("http://localhost:8080/api/v1/products/" + productId)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ResourceNotFoundException("Product not found");
        }

        Review review = mapReviewRequestToReview(reviewRequest);
        review.setProductId(productId);

        try {
            User user = webClient.get()
                    .uri("http://localhost:8080/api/v1/users/" + reviewRequest.getUserId())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            String[] name = user.getFullName().split(" ");
            reviewRepository.save(review);
            ReviewResponse response = mapReviewToReviewResponse(review);
            response.setUserName(name[0]);
            return response;
        } catch (WebClientResponseException e) {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public List<ReviewResponse> getAllReviewByProduct(Long productId) {

        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(this::mapReviewToReviewResponse).toList();



        return reviewResponses;
    }

    private Review mapReviewRequestToReview(ReviewRequest reviewRequest) {
        Review review = Review.builder()
                .userId(reviewRequest.getUserId())
                .title(reviewRequest.getTitle())
                .description(reviewRequest.getDescription())
                .rate(reviewRequest.getRate())
                .build();
        return review;
    }
    private ReviewResponse mapReviewToReviewResponse(Review review) {
        User user = webClient.get()
                .uri("http://localhost:8080/api/v1/users/" + review.getUserId())
                .retrieve()
                .bodyToMono(User.class)
                .block();
        String[] name = user.getFullName().split(" ");
        ReviewResponse response = ReviewResponse.builder()
                .id(review.getId())
                .userName(name[0])
                .title(review.getTitle())
                .description(review.getDescription())
                .rate(review.getRate())
                .createdAt(review.getCreatedAt())
                .build();
        return response;
    }
}
