package com.melck.productservice.client;

import com.melck.productservice.dto.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReviewClient {

    private final WebClient webClient;
    @Value("${app-config.client.review}")
    private final String reviewUri;

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
