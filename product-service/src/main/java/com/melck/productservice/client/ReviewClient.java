package com.melck.productservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melck.productservice.dto.Review;
import com.melck.productservice.services.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReviewClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${app-config.client.review}")
    private String reviewUri;

    public Review[] getReviewByProductId(Long id) {

          log.info("Searching Reviews to product with id:{} into Review API", id);
          try {
              log.info("Returning product id: " + id + " with your reviews list");
              return webClient
                      .get()
                      .uri(buildUri(id))
                      .retrieve()
                      .bodyToMono(Review[].class)
                      .block();
          } catch (Exception e) {
              log.error("error fetching review with id:" + id + " into review service." + e.getMessage(), e);
              throw e;
          }
    }

    private String buildUri(Long id) {
        return String.format(reviewUri, id.toString());
    }
}
