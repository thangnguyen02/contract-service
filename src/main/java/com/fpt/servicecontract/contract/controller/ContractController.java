package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.SearchRequestBody;
import com.fpt.servicecontract.contract.dto.SignContractDTO;
import com.fpt.servicecontract.contract.model.ContractStatus;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.contract.service.ContractStatusService;
import com.fpt.servicecontract.contract.service.ElasticSearchService;
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
import java.util.Arrays;
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
    public String sendMail(@RequestHeader("Authorization") String bearerToken, @RequestParam String[] to,
                           @RequestParam(required = false) String[] cc,
                           @RequestParam String subject,
                           @RequestParam String htmlContent,
                           @RequestParam(required = false) MultipartFile[] attachments,
                           @RequestParam String contractId) throws MessagingException {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        //Contract status
        List<String> receivers = new ArrayList<>();
        for (String recipient : to) {
            receivers.add(recipient.trim());
        }
       if(cc!=null){
           for (String recipient : cc) {
               receivers.add(recipient.trim());
           }
       }
        contractStatusService.create(email, receivers, contractId);
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
    public ResponseEntity<BaseResponse> findAll(@RequestHeader("Authorization") String bearerToken, @PathVariable int page, @PathVariable int size) {
        Pageable p = PageRequest.of(page, size);
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(contractService.findAll(p, email));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('PERMISSION_SALE') || hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> createContract(@RequestHeader("Authorization") String bearerToken,
                                                       @RequestBody ContractRequest contractRequest) throws Exception {
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
    public ResponseEntity<BaseResponse> getContractSignById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, contractService.getContractSignById(id)));
    }

    @PostMapping("/public/sign-contract")
    public ResponseEntity<BaseResponse> signContract(@RequestBody SignContractDTO signContractDTO) throws Exception {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, contractService.signContract(signContractDTO)));
    }

    @PostMapping("/reject-contract")
    public ResponseEntity<BaseResponse> rejectContract(@RequestBody SearchRequestBody searchRequestBody) throws IOException {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, elasticSearchService.search("contract", searchRequestBody, ContractRequest.class)));
    }
}
