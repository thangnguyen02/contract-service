package com.fpt.servicecontract.config;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

public interface MailService {
    Future<Void> sendNewMail(String[] to, String[] cc, String subject, String htmlContent, MultipartFile[] attachments) throws MessagingException;
}
