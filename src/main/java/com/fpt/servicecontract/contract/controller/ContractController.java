package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.service.ContractService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

  private final MailService mailService;
  private final ContractService contractService;

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

  @PostMapping("/create-old-contract")
  public ResponseEntity<String> uploadImages(@RequestParam("content") String content,
                                             @RequestParam("images") List<MultipartFile> images)   {
    return ResponseEntity.ok(contractService.createOldContract(content, images));
  }
}
