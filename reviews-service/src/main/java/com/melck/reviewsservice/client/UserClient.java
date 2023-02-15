package com.melck.reviewsservice.client;

import com.melck.reviewsservice.dto.User;
import com.melck.reviewsservice.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
        } catch (WebClientResponseException e) {
            throw new ResourceNotFoundException("User not found");
        }

    }
    private String buildUri(Long id) {
        return String.format(userServiceUri, id.toString());
    }

}
