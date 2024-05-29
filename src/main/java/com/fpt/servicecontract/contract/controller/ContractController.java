package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.ContractDto;
import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.SearchContractRequest;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

  @PostMapping()
  public ResponseEntity<BaseResponse> createContract(@RequestBody ContractRequest contractRequest) {
    return ResponseEntity.ok(contractService.createContract(contractRequest));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ContractDto> updateContract(@PathVariable String id, @RequestBody ContractDto contractDto) {
    return null;
  }
}
