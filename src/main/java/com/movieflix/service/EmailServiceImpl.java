package com.movieflix.service;

import com.movieflix.dto.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

//        MimeMessage mineMessage = javaMailSender.createMimeMessage();  //to add attachments
//        MimeMessageHelper helper = new MimeMessageHelper(mineMessage, true);
//        helper.setFrom(fromEmail);
//        helper.setTo(mailBody.to());
//        helper.setSubject(mailBody.subject());
//        helper.setText(mailBody.text());
//        helper.addAttachment("give file name", new File("give file path"));

        javaMailSender.send(message);
    }
}
