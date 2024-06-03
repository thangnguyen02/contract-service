package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final MailService mailService;
    private final ContractService contractService;
    private final JwtService jwtService;

    @PostMapping("/send-mail")
    public String sendMail(@RequestParam String[] to,
                           @RequestParam(required = false) String[] cc,
                           @RequestParam String subject,
                           @RequestParam String htmlContent,
                           @RequestParam(required = false) MultipartFile[] attachments) throws MessagingException {
        mailService.sendNewMail(to, cc, subject, htmlContent, attachments);
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
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(contractService.findById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(contractService.delete(id));
    }
    @GetMapping("/{page}/{size}")
    public ResponseEntity<BaseResponse> findAll(@PathVariable int page, @PathVariable int size) {
        Pageable p = PageRequest.of(page, size);
        return ResponseEntity.ok(contractService.findAll(p));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('PERMISSION_SALE') || hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> createContract(@RequestHeader("Authorization") String bearerToken,
                                                       @RequestBody ContractRequest contractRequest) throws Exception {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(contractService.createContract(contractRequest, email));
    }
}
