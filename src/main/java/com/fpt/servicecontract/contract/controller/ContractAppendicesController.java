package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.contract.dto.SignContractDTO;
import com.fpt.servicecontract.contract.model.ContractAppendices;
import com.fpt.servicecontract.contract.service.ContractAppendicesService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/contract-appendices")
@RequiredArgsConstructor
public class ContractAppendicesController {
    private final ContractAppendicesService service;
    private final JwtService jwtService;


    @GetMapping("/{page}/{size}")
    public ResponseEntity<BaseResponse> getAll(@RequestHeader("Authorization") String bearerToken,
                                               @RequestParam(required = false) String status,
                                               @RequestParam String contractId,
                                               @PathVariable int page, @PathVariable int size) {
        Pageable p = PageRequest.of(page, size);
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(service.getAll(p, email, status, contractId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestHeader("Authorization") String bearerToken,
                                               @RequestBody ContractAppendices contractAppendices){
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(service.save(contractAppendices, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable String id, @RequestBody ContractAppendices contractAppendices) {
        return ResponseEntity.ok(service.update(id, contractAppendices));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @GetMapping("/contractId/{id}/{page}/{size}")
    public ResponseEntity<BaseResponse> getContractById(
            @PathVariable String id,
            @PathVariable int page,
            @PathVariable int size
    ) {
        return ResponseEntity.ok(service.getByContractId(id, page, size));
    }

    @PostMapping("/send-mail")
    public ResponseEntity<BaseResponse> sendMail(@RequestHeader("Authorization") String bearerToken,
                                                         @RequestParam String[] to,
                                                         @RequestParam(required = false) String[] cc,
                                                         @RequestParam String subject,
                                                         @RequestParam String htmlContent,
                                                         @RequestParam(required = false) MultipartFile[] attachments,
                                                         @RequestParam String contractAppendicesId,
                                                         @RequestParam String status,
                                                         @RequestParam String description,
                                                 @RequestParam String reasonId
    ) {
        return ResponseEntity.ok(service.sendMail(bearerToken, to, cc, subject, htmlContent, attachments, contractAppendicesId, status, description,reasonId));
    }

    @PostMapping("/public/send-mail")
    public ResponseEntity<BaseResponse> sendMail(
            @RequestParam String[] to,
            @RequestParam(required = false) String[] cc,
            @RequestParam String subject,
            @RequestParam String htmlContent,
            @RequestParam String createdBy,
            @RequestParam String contractAppendicesId,
            @RequestParam String status,
            @RequestParam String description
    )  {
        return ResponseEntity.ok(service.publicSendMail(to, cc, subject, htmlContent, createdBy, contractAppendicesId, status, description));
    }

    @GetMapping("/public/sign-contract-appendices/{id}")
    public ResponseEntity<BaseResponse> getContractSignById(@PathVariable String id) {
        return ResponseEntity.ok(service.getContractSignById(id));
    }

    @PostMapping("/public/sign-contract-appendices")
    public ResponseEntity<BaseResponse> signContractAppendices(@RequestBody SignContractDTO signContractDTO) {
        return ResponseEntity.ok(service.signContract(signContractDTO));
    }
}