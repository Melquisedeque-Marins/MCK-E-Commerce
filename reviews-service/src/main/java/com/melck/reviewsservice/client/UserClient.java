package com.melck.reviewsservice.client;

import com.melck.reviewsservice.dto.User;
import com.melck.reviewsservice.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserClient {
    private final WebClient webClient;
    @Value("${app-config.client.user}")
    private String userServiceUri;

    public User getUserInUserService(Long id) {
        try {
            return webClient.get()
                    .uri(buildUri(id))
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
        } catch (Exception e) {
            log.error("error when Searching a user in a user service " + e.getMessage(), e);
            throw e;
        }
    }
    private String buildUri(Long id) {
        return String.format(userServiceUri, id.toString());
    }

}
