package com.melck.userservice.service;

import com.melck.userservice.client.CartClient;
import com.melck.userservice.dto.*;
import com.melck.userservice.entity.User;
import com.melck.userservice.repository.UserRepository;
import com.melck.userservice.service.exception.CpfAlreadyInUseException;
import com.melck.userservice.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final CartClient cartClient;

    @Transactional
    public UserResponse registerUser(UserRequest userRequest) {
        log.info("Verifying if cpf is already in use ");
        User existentUser = userRepository.findByCpf(userRequest.getCpf());
        if (existentUser != null) {
            log.error("The CPF:" + userRequest.getCpf() + " is already in use ");
            throw new CpfAlreadyInUseException("The CPF: " + userRequest.getCpf() + " is already in use ");
        }
        log.info("Creating a new user");
        User user = modelMapper.map(userRequest, User.class);
        Long cartId = cartClient.getCartId();
        user.setCartId(cartId);
        log.info("User created successfully");
        return mapUserToUserResponse(userRepository.save(user));
    }


    public String userCreation(UserCreationRequest userRequest) {

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", "spring-cloud-gateway-client");
            map.add("client_secret", "SvxFR2To1LfqQvJroCghVXX3vl3GoYFd");
            map.add("grant_type", "client_credentials");

        TokenResponse accessToken = webClient.post()
                .uri("localhost:8088/realms/mck-e-commerce/protocol/openid-connect/token")
//                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(map))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        String response = webClient.post()
                .uri("localhost:8088/admin/realms/mck-e-commerce/users")
                .header("Authorization", "Bearer " + accessToken.getAccess_token())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;

    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("Searching user in the database ");
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> {
            log.error("User not found for id: {}",id);
            return new UserNotFoundException("User not found " + id);
        });
        log.info("Returning user with id: id", user);
        return mapUserToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapUserToUserResponse).toList();
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        log.info("Returning user", response);
        return response;
    }
}
