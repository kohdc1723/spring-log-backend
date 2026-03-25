package org.example.springlogbackend.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${com.sendgrid.api.key}")
    private String apiKey;
    @Value("${com.sendgrid.from.email}")
    private String fromEmail;
    @Value("${FRONTEND_BASE_URL}")
    private String frontendBaseUrl;

    public void sendVerificationEmail(String to, String token) {
        String link = frontendBaseUrl + "/email-verification?token=" + token;
        String subject = "Verification Email";
        String text = "Please verify your email address by clicking on the link below.\n\n" + link;
        sendEmail(to, subject, text);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String link = frontendBaseUrl + "/reset-password?token=" + token;
        String subject = "Reset Password";
        String text = "Please reset your password by clicking on the link below.\n\n" + link;
        sendEmail(to, subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        Email fromEmail = new Email(this.fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.EMAIL_SEND_ERROR);
        }
    }
}
