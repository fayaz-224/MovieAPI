package com.movieflix.services;

import com.movieflix.dto.MailBody;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendSimpleMessage(MailBody body);
    void sendSimpleMessageWithAttachments(MailBody body) throws MessagingException;
}
