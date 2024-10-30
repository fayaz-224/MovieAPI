package com.movieflix.service;

import com.movieflix.dto.MailBody;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendSimpleMessage(MailBody body) throws MessagingException;
}
