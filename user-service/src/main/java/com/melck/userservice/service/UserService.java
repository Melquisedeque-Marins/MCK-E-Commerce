package com.melck.userservice.service;

import com.melck.userservice.dto.Cart;
import com.melck.userservice.dto.UserResponse;
import com.melck.userservice.entity.User;
import com.melck.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    @Transactional
    public UserResponse registerUser(User user) {

        Cart cart = webClient.post()
                .uri("http://localhost:8081/api/v1/cart")
                .retrieve()
                .bodyToMono(Cart.class)
                .block();

        user.setCartId(cart.getId());

        return mapUserToUserResponse(userRepository.save(user));
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}
