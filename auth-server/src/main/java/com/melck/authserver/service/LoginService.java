package com.melck.authserver.service;

import com.melck.authserver.model.LoginRequest;
import com.melck.authserver.model.LoginResponse;
import com.melck.authserver.model.Response;
import com.melck.authserver.model.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issueUrl;
    @Value("${spring.security.oauth2.client.provider.keycloak.logout-uri}")
    private String logoutUrl;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.authorization-grant-type}")
    private String grantType;




    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
        map.add("username", loginRequest.getUsername());
        map.add("password", loginRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map,headers);

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(issueUrl, httpEntity, LoginResponse.class);
        return ResponseEntity.ok(response.getBody());
    }

    public ResponseEntity<Response> logout(TokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", request.getToken());


        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<Response> response = restTemplate.postForEntity(logoutUrl, httpEntity, Response.class);

        Response res = new Response();
        if (response.getStatusCode().is2xxSuccessful()) {
            res.setMessage("Logged out successfully");
        }
        return ResponseEntity.ok(res);
    }
}
