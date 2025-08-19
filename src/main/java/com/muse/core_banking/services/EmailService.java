package com.muse.core_banking.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendVerifyEmailMail(
            String to,
            String otp,
            String templateName,
            String subject
    ) throws MessagingException {
        Context context = new Context();
        context.setVariable("otp", otp);

        String htmlContent = templateEngine.process(templateName, context);

        sendMail(to, subject, htmlContent );
    }

    private void sendMail(
            String to,
            String subject,
            String content
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("olysegs@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
