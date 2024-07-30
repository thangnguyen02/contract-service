package com.fpt.servicecontract.contract.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.*;
import com.fpt.servicecontract.contract.dto.request.ContractRequest;
import com.fpt.servicecontract.contract.dto.request.SearchRequestBody;
import com.fpt.servicecontract.contract.dto.response.SignContractResponse;
import com.fpt.servicecontract.contract.service.*;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final MailService mailService;
    private final ContractService contractService;
    private final JwtService jwtService;
    private final ElasticSearchService elasticSearchService;
    private final ContractStatusService contractStatusService;


    @PostMapping("/send-mail")
    public ResponseEntity<BaseResponse> sendMail(@RequestHeader("Authorization") String bearerToken,
                                                 @RequestParam String[] to,
                                                 @RequestParam(required = false) String[] cc,
                                                 @RequestParam String subject,
                                                 @RequestParam String htmlContent,
                                                 @RequestParam(required = false) MultipartFile[] attachments,
                                                 @RequestParam String contractId,
                                                 @RequestParam String status,
                                                 @RequestParam String description
    ) {
        return ResponseEntity.ok(contractService.sendMail(bearerToken, to, cc, subject, htmlContent, attachments, contractId, status, description));
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
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS, "", true, contractService.findById(id)));
    }

    @GetMapping("/party/{id}")
    public ResponseEntity<BaseResponse> findContractPartyById(@PathVariable String id) {
        return ResponseEntity.ok(contractService.findContractPartyById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(contractService.delete(id));
    }

    @GetMapping("/{page}/{size}")
    public ResponseEntity<BaseResponse> findAll(@RequestHeader("Authorization") String bearerToken,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) String search,
                                                @PathVariable int page, @PathVariable int size) throws JsonProcessingException {
        Pageable p = PageRequest.of(page, size);
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(contractService.findAll(p, email, status, search));
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createContract(@RequestHeader("Authorization") String bearerToken,
                                                       @RequestBody ContractRequest contractRequest) {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(contractService.createContract(contractRequest, email));
    }

    @PostMapping("/search")
    public ResponseEntity<BaseResponse> searchContracts(@RequestBody SearchRequestBody searchRequestBody) throws IOException {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, elasticSearchService.search("contract", searchRequestBody, ContractRequest.class)));
    }

    @GetMapping("/sync")
    public ResponseEntity<Void> sync() throws IOException {
        return ResponseEntity.ok(contractService.sync());
    }

    @GetMapping("/public/sign-contract/{id}")
    public ResponseEntity<BaseResponse> getContractSignById(@PathVariable String id) {
        return ResponseEntity.ok(contractService.getContractSignById(id));
    }

    @PostMapping("/public/sign-contract")
    public ResponseEntity<BaseResponse> signContract(@RequestBody SignContractDTO signContractDTO) {
        return ResponseEntity.ok(contractService.signContract(signContractDTO));
    }

    @PostMapping("/reject-contract")
    public ResponseEntity<BaseResponse> rejectContract(@RequestBody SearchRequestBody searchRequestBody) throws IOException {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, elasticSearchService.search("contract", searchRequestBody, ContractRequest.class)));
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

    @GetMapping("/getNumberContractNoti")
    public ResponseEntity<BaseResponse> getNotificationContractNumber(@RequestHeader("Authorization") String bearerToken) {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(contractService.getNotificationContractNumber(email));
    }
}
