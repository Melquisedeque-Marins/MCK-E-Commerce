package com.melck.notificationservice.service;

import com.melck.notificationservice.dto.User;
import com.melck.notificationservice.entity.Email;
import com.melck.notificationservice.enums.StatusEmail;
import com.melck.notificationservice.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
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
    public Email sendEmail(User user) {

        String name = user.getFullName();
        String[] firstName = name.split(" ");
        String userName = firstName[0];

        Email email = new Email();
        email.setSendDateEmail(LocalDateTime.now());
        email.setEmailTo(user.getEmail());
        email.setOwnerRef(userName);

        email.setSubject("Account registered successfully");
        email.setEmailFrom("mck.enterprises.clinic@gmail.com");
        email.setText("<h1>Olá, <style='text-transform:capitalize;'>" + userName + "</h1>"
                + "\n Tou are now part of MCK-E-Commerce family. \n"
                + "<img src='https://www.feedz.com.br/blog/wp-content/uploads/2021/10/mensagem-de-boas-vindas-para-novos-colaboradores-1.webp' alt='teste'/> ");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getEmailFrom());
            helper.setTo(user.getEmail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(),true);
            helper.addAttachment("equipe.jpg", new ClassPathResource("/static/img/equipe.jpg"));
            mailSender.send(message);
            email.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e) {
            email.setStatusEmail(StatusEmail.ERROR);
        } catch (MessagingException e) {
            email.setStatusEmail(StatusEmail.ERROR);
        }
        return repository.save(email);

    }
}
