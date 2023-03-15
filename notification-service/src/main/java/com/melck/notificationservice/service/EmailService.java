package com.melck.notificationservice.service;

import com.melck.notificationservice.dto.UserNotification;
import com.melck.notificationservice.entity.Email;
import com.melck.notificationservice.enums.StatusEmail;
import com.melck.notificationservice.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EmailService {

    private EmailRepository repository;
    private JavaMailSender mailSender;

    @RabbitListener(queues = "users.v1.user-created")
    public void sendEmail(UserNotification user) {

        String name = user.getFullName();
        String[] firstName = name.split(" ");
        String username = firstName[0];

        Email email = new Email();
        email.setSendDateEmail(LocalDateTime.now());
        email.setEmailTo(user.getEmail());
        email.setOwnerRef(username);

        email.setSubject("Account registered successfully");
        email.setEmailFrom("mck.enterprises.clinic@gmail.com");
        email.setText("<h1>Olá, <style='text-transform:capitalize;'>" + username + "</h1>"
                + "\n You are now part of MCK-E-Commerce family. \n");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getEmailFrom());
            helper.setTo(user.getEmail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(),true);
            mailSender.send(message);
            email.setStatusEmail(StatusEmail.SENT);
        } catch (MailException | MessagingException e) {
            email.setStatusEmail(StatusEmail.ERROR);
        }
        repository.save(email);
    }
}
