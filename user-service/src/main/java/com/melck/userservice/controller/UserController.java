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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @CircuitBreaker(name = "cart", fallbackMethod = "fallbackMethod")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest userRequest) {
        String response = userService.registerUser(userRequest);
        return ResponseEntity.ok(response);
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

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(Principal principal) {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        String userFamilyName = (String) token.getTokenAttributes().get("family_name");
        Instant tokenExp = (Instant) token.getTokenAttributes().get("exp");
        String accessToken = (String) token.getToken().getTokenValue();
//        return ResponseEntity.ok("Hello Admin \nUser Name : " + userName + "\nUser Email : " + userEmail + "\nUser Familyname : " + userFamilyName + "\ntokenexp" + tokenExp );
        return ResponseEntity.ok("access token " + accessToken);
    }

}
