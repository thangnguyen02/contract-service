package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

  private final MailService mailService;

  @PostMapping("/send-mail")
  public String sendMail(@RequestParam String[] to,
      @RequestParam(required = false) String[] cc,
      @RequestParam String subject,
      @RequestParam String htmlContent,
      @RequestParam(required = false) MultipartFile[] attachments) throws MessagingException {
    mailService.sendNewMail(to, cc, subject, htmlContent ,attachments);
    return "SEND OK";
  }

  @GetMapping("/test-role")
  @PreAuthorize("hasRole('ROLE_USER')")
  public String test() {
    return "ROLE_USER";
  }

  @GetMapping("/test-permission")
  @PreAuthorize("hasAuthority('PERMISSION_SALE')")
  public String tests() {
    return "PERMISSION_SALE";
  }
}
