package com.melck.userservice.service;

import com.melck.userservice.client.CartClient;
import com.melck.userservice.dto.*;
import com.melck.userservice.entity.User;
import com.melck.userservice.repository.UserRepository;
import com.melck.userservice.service.exception.AttributeAlreadyInUseException;
import com.melck.userservice.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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
    private final BCryptPasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    private final static String EXCHANGE = "users.v1.user-created";

    @Value("${keycloak-client-id}")
    private String clientId;
    @Value("${keycloak-client-secret}")
    private String clientSecret;
    @Value("${keycloak-token-uri}")
    private String tokenUri;
    @Value("${keycloak-user-uri}")
    private String userUri;

    @Transactional
    public UserResponse registerUser(UserRequest userRequest) {
        log.info("Verifying if cpf is already in use ");
        User existentUser = userRepository.findByCpf(userRequest.getCpf());
        if (existentUser != null) {
            log.error("The CPF:" + userRequest.getCpf() + " is already in use ");
            throw new AttributeAlreadyInUseException("The CPF: " + userRequest.getCpf() + " is already in use ");
        }
        log.info("Creating a new user");
        User user = modelMapper.map(userRequest, User.class);
        Long cartId = cartClient.getCartId();
        user.setCartId(cartId);
        log.info("User created successfully");
        var newUser = userRepository.save(user);
        String routingKey = "users.v1.user-created";
        rabbitTemplate.convertAndSend(routingKey, newUser);
        return mapUserToUserResponse(userRepository.save(newUser));
    }

    @Transactional
    public String registerUserLocalAndKeycloak(UserRequest userRequest) {

        User existentUser = userRepository.findByCpf(userRequest.getCpf());
        if (existentUser != null) {
            log.error("The CPF:" + userRequest.getCpf() + " is already in use ");
            throw new AttributeAlreadyInUseException("The CPF: " + userRequest.getCpf() + " is already in use ");
        }


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "client_credentials");

        var credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(userRequest.getPassword());

        String[] name = userRequest.getFullName().split(" ");

        UserCreationRequest newUser = new UserCreationRequest();
        newUser.setEnabled(true);
        newUser.setEmail(userRequest.getEmail());
        newUser.setEmailVerified("");
        newUser.setFirstName(name[0]);
        newUser.setLastName(name[name.length - 1]);
        newUser.setUsername(userRequest.getUsername());
        newUser.getCredentials().add(credential);

        try {
            TokenResponse accessToken = webClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(map))
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .onErrorResume(e -> Mono.empty())
                    .block();

            if (accessToken == null){
                log.error("An error occurred while trying to get the token");
                throw new AttributeAlreadyInUseException("Error getting a token");
            }

            log.info("Trying create user in Keycloak");
            var responseMessage = webClient.post()
                    .uri(userUri)
                    .header("Authorization", "Bearer " + accessToken.getAccess_token())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(newUser)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class) // error body as String or other class
                            .flatMap(error -> Mono.error(new AttributeAlreadyInUseException(error)))) // throw a functional exception
                    .bodyToMono(String.class)
                    .block();

            if (responseMessage == null ){
                log.info("Creating a new user");
                Long cartId = cartClient.getCartId();
                User user = modelMapper.map(userRequest, User.class);
                user.setCartId(cartId);
                user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                userRepository.save(user);
                log.info("User created successfully");
                log.info("Sent a message to fanOutExchange");
                var userNotification = modelMapper.map(user, UserNotification.class);
                rabbitTemplate.convertAndSend(EXCHANGE, "", userNotification);
                return "user created successfully";
            }
            return responseMessage;

        } catch (WebClientResponseException e ) {
            log.error("Error went trying to request user creation");
            throw new AttributeAlreadyInUseException("");
        }
    }
    public String registerUserInKeycloak(UserCreationRequest userRequest) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "client_credentials");

        try {
            TokenResponse accessToken = webClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(map))
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .onErrorResume(e -> Mono.empty())
                    .block();

            if (accessToken == null){
                log.error("An error occurred while trying to get the token");
                throw new AttributeAlreadyInUseException("Error getting a token");
            }
            return webClient.post()
                    .uri(userUri)
                    .header("Authorization", "Bearer " + accessToken.getAccess_token())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(userRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class) // error body as String or other class
                            .flatMap(error -> Mono.error(new AttributeAlreadyInUseException(error)))) // throw a functional exception
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e ) {
            log.error("Error went trying to request user creation");
            throw new AttributeAlreadyInUseException("");
        }
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("Searching user in the database ");
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> {
            log.error("User not found for id: {}",id);
            return new UserNotFoundException("User not found " + id);
        });
        log.info("Returning user with id: {} {}", id, user);
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
        log.info("Returning user {}", response);
        return response;
    }
}
