package com.melck.notificationservice.service;

import com.melck.notificationservice.dto.UserNotification;
import com.melck.notificationservice.entity.Email;
import com.melck.notificationservice.enums.StatusEmail;
import com.melck.notificationservice.repository.EmailRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final EmailRepository repository;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Transactional
    @RabbitListener(queues = "users.v1.user-created.send-notification")
    public String sendEmail(UserNotification user) {

        String name = user.getFullName();
        String[] fullName = name.split(" ");
        String username = fullName[0];

        Email email = new Email();
        email.setSendDateEmail(LocalDateTime.now());
        email.setEmailTo(user.getEmail());
        email.setOwnerRef(username);
        email.setSubject("Account created successfully");
        email.setEmailFrom(sender);
        email.setText("<h1>Hi, <style='text-transform:capitalize;'>" + username + "</h1>"
                + "\n You are now part of the MCK family. Take advantage of our offers. \n");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getEmailFrom());
            helper.setTo(user.getEmail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(), true);
//            helper.addAttachment("equipe.jpg", new ClassPathResource("/static/img/equipe.jpg"));
            mailSender.send(message);
            email.setStatusEmail(StatusEmail.SENT);
        } catch (Exception e) {
            email.setStatusEmail(StatusEmail.ERROR);
            repository.save(email);
            return "Error. Something went wrong";
        }
        repository.save(email);
        return "Mail successfully sent";
    }
}
