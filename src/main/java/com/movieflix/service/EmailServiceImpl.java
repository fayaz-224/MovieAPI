package com.movieflix.service;

import com.movieflix.dto.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.emailSender = javaMailSender;
    }

    @Override
    @Async //runs in separate threads
    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        emailSender.send(message);
    }

    @Override
    @Async
    public void sendSimpleMessageWithAttachments(MailBody body) throws MessagingException {
        MimeMessage mineMessage = emailSender.createMimeMessage();  //to add attachments
        MimeMessageHelper helper = new MimeMessageHelper(mineMessage, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(body.to());
        helper.setSubject(body.subject());
        helper.setText(body.text());
        helper.addAttachment("give fileName", new File("give file path"));

        emailSender.send(mineMessage);
    }
}
