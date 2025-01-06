package org.example.emailservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailSender {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailSender(JavaMailSender mailSender,
                       @Value("${email-address}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendRegistrationEmail(String email, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(email);
            message.setText("Welcome to our social media platform, " + username + "!");
            message.setSubject("Welcome to our social media platform!");

            mailSender.send(message);
        } catch (Exception e) {
            log.warn("Failed to send registration email for {} to {}", username, email, e);
        }
    }
}
