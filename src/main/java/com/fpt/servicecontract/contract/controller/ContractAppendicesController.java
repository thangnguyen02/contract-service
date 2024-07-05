package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.SignContractResponse;
import com.fpt.servicecontract.contract.model.ContractAppendices;
import com.fpt.servicecontract.contract.service.ContractAppendicesService;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.contract.service.ContractStatusService;
import com.fpt.servicecontract.utils.BaseResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contract-appendices")
@RequiredArgsConstructor
public class ContractAppendicesController {
    private final ContractAppendicesService service;
    private final JwtService jwtService;
    private final MailService mailService;
    private final ContractStatusService contractStatusService;


    @GetMapping("/{page}/{size}")
    public ResponseEntity<BaseResponse> getAll(@RequestHeader("Authorization") String bearerToken,
                                           @RequestParam(required = false) String status,
                                           @PathVariable int page, @PathVariable int size)
    {
        Pageable p = PageRequest.of(page, size);
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(service.getAll(p, email, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestBody ContractAppendices contractAppendices) {
        return ResponseEntity.ok(service.save(contractAppendices));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable String id, @RequestBody ContractAppendices contractAppendices) {
        return ResponseEntity.ok(service.update(id, contractAppendices));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @PostMapping("/send-mail")
    public ResponseEntity<SignContractResponse> sendMail(@RequestHeader("Authorization") String bearerToken,
                                                         @RequestParam String[] to,
                                                         @RequestParam(required = false) String[] cc,
                                                         @RequestParam String subject,
                                                         @RequestParam String htmlContent,
                                                         @RequestParam(required = false) MultipartFile[] attachments,
                                                         @RequestParam String contractId,
                                                         @RequestParam String status,
                                                         @RequestParam String description
    ) throws MessagingException {
        return ResponseEntity.ok(service.sendMail(bearerToken, to, cc, subject, htmlContent, attachments, contractId, status, description));
    }

    @PostMapping("/public/send-mail")
    public SignContractResponse sendMail(
            @RequestParam String[] to,
            @RequestParam(required = false) String[] cc,
            @RequestParam String subject,
            @RequestParam String htmlContent,
            @RequestParam String createdBy,
            @RequestParam String contractId,
            @RequestParam String status,
            @RequestParam String description
    ) throws MessagingException {
        SignContractResponse signContractResponse = new SignContractResponse();
        //Contract status
        List<String> receivers = new ArrayList<>();
        for (String recipient : to) {
            receivers.add(recipient.trim());
        }
        if (cc != null) {
            for (String recipient : cc) {
                receivers.add(recipient.trim());
            }
        }
        contractStatusService.create(createdBy, receivers, contractId, status, description);
        mailService.sendNewMail(to, cc, subject, htmlContent, null);
        return signContractResponse;
    }
}