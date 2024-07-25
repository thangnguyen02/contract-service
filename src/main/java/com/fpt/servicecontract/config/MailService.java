package com.fpt.servicecontract.config;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

public interface MailService {
    void sendNewMail(String[] to, String[] cc, String subject, String htmlContent
            , MultipartFile[] attachments
    ) throws MessagingException;
}
