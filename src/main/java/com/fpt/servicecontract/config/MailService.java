package com.fpt.servicecontract.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    public void sendNewMail(String[] to, String[] cc, String subject, String htmlContent
        , MultipartFile[] attachments
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        mailSender.send(message);
    }
}
