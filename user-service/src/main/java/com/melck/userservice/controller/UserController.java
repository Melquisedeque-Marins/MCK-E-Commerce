package com.melck.userservice.controller;

import com.melck.userservice.dto.UserCreationRequest;
import com.melck.userservice.dto.UserRequest;
import com.melck.userservice.dto.UserResponse;
import com.melck.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @CircuitBreaker(name = "cart", fallbackMethod = "fallbackMethod")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse newUser = userService.registerUser(userRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @PostMapping("/key")
    public ResponseEntity<String> registerUserKey(@Valid @RequestBody UserCreationRequest userRequest) {
        String response = userService.registerUserInKeycloak(userRequest);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> fallbackMethod(WebClientResponseException e) {
        log.info("Oops! Something went wrong, the cart service is down. Please try again later.", e);
        return ResponseEntity.ok().body("Oops! Something went wrong, the cart service is down. Please try again later.");
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
}
