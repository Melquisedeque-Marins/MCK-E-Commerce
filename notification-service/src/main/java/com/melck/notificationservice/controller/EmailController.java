package com.melck.notificationservice.controller;

import com.melck.notificationservice.dto.UserNotification;
import com.melck.notificationservice.entity.Email;
import com.melck.notificationservice.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody UserNotification user) {
        String email = emailService.sendEmail(user);
        return ResponseEntity.ok(email);
    }
}
