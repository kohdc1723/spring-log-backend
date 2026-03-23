package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${FRONTEND_BASE_URL}")
    private String frontendBaseUrl;

    public void sendVerificationEmail(String to, String token) {
        String link = frontendBaseUrl + "/email-verification?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Please verify your email address by clicking on the link below.\n\n" + link);

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String link = frontendBaseUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Please reset your password by clicking on the link below.\n\n" + link);

        mailSender.send(message);
    }
}
