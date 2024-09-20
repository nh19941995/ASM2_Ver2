package com.example.demo.service.email;

import com.example.demo.service.UserService;
import com.example.demo.view.ViewConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendLoginNotification(String email, String username, Date loginTime) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Xác nhận đăng nhập thành công");

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("message", "Thời gian đăng nhập: " + loginTime);

        String htmlContent = templateEngine.process(ViewConstants.EMAIL_TEMPLATE, context);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    public void sendEmailAuthentication(
            String email,
            String username,
            String token
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Xác thực email");

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("token", token);

        String htmlContent = templateEngine.process(ViewConstants.EMAIL_VERIFICATION, context);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}