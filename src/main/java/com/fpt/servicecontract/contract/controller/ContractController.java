package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.*;
import com.fpt.servicecontract.contract.enums.SignContractStatus;
import com.fpt.servicecontract.contract.model.Contract;
import com.fpt.servicecontract.contract.model.Notification;
import com.fpt.servicecontract.contract.repository.ContractRepository;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.contract.service.ContractStatusService;
import com.fpt.servicecontract.contract.service.ElasticSearchService;
import com.fpt.servicecontract.contract.service.NotificationService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final ContractRepository contractRepository;
    private final NotificationService notificationService;

    @PostMapping("/send-mail")
    public SignContractResponse sendMail(@RequestHeader("Authorization") String bearerToken,
                                         @RequestParam String[] to,
                                         @RequestParam(required = false) String[] cc,
                                         @RequestParam String subject,
                                         @RequestParam String htmlContent,
                                         @RequestParam(required = false) MultipartFile[] attachments,
                                         @RequestParam String contractId,
                                         @RequestParam String status,
                                         @RequestParam String description
    ) throws MessagingException {
        SignContractResponse signContractResponse = new SignContractResponse();
        String email = jwtService.extractUsername(bearerToken.substring(7));
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
        Optional<Contract> contract = contractRepository.findById(contractId);
//        //màn hình hợp đồng của OFFICE_ADMIN:
//         btn phê duyệt hợp đồng : OFFICE_ADMIN approve thì sale sẽ enable btn gửi cho MANAGER (approve rồi disable)
        if (status.equals(SignContractStatus.WAIT_APPROVE.name())) {
            signContractResponse.setCanSendForMng(false);
            signContractResponse.setCanSend(false);

            notificationService.create(Notification.builder()
                            .title(contract.get().getName())
                            .message("Bạn có hợp đồng mới cần kiểm tra")
                            .typeNotification("CONTRACT")
                            .receivers(receivers)
                            .sender(email)
                    .build());
        }
        if (status.equals(SignContractStatus.APPROVED.name())) {
            String approved = jwtService.extractUsername(bearerToken.substring(7));
            if (contract.isPresent()) {
                contract.get().setApprovedBy(approved);
                contractRepository.save(contract.get());
            }
            signContractResponse.setCanSendForMng(true);
            signContractResponse.setCanSend(false);
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + "đã duyệt hợp đồng")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }

        //officer-admin reject
        if (status.equals(SignContractStatus.APPROVE_FAIL.name())) {
            signContractResponse.setCanSend(true);
            signContractResponse.setCanSendForMng(false);
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + "đã yêu cầu xem lại hợp đồng")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }

        // site a or b reject with reseon
        if (status.equals(SignContractStatus.SIGN_B_FAIL.name())
                || status.equals(SignContractStatus.SIGN_A_FAIL.name())
        ) {
            signContractResponse.setCanSend(true);
            signContractResponse.setCanSendForMng(false);
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + "đã từ chối kí hợp đồng")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }
        List<String> statusDb = contractStatusService.checkDoneSign(contractId);

        if (status.equals(SignContractStatus.SIGN_A_OK.name())
        ) {
            signContractResponse.setCanSend(false);
            signContractResponse.setCanSendForMng(false);
            if (statusDb.contains(SignContractStatus.SIGN_B_OK.name())) {
                status = SignContractStatus.SUCCESS.name();
                notificationService.create(Notification.builder()
                        .title(contract.get().getName())
                        .message(email + "đã kí hợp đồng thành công")
                        .typeNotification("CONTRACT")
                        .receivers(receivers)
                        .sender(email)
                        .build());
            }

        }

        if (status.equals(SignContractStatus.SIGN_B_OK.name())
        ) {
            signContractResponse.setCanSend(false);
            signContractResponse.setCanSendForMng(false);
            if (statusDb.contains(SignContractStatus.SIGN_A_OK.name())) {
                status = SignContractStatus.SUCCESS.name();
                notificationService.create(Notification.builder()
                        .title(contract.get().getName())
                        .message(email + "đã kí hợp đồng thành công")
                        .typeNotification("CONTRACT")
                        .receivers(receivers)
                        .sender(email)
                        .build());
            }

        }


        if (status.equals(SignContractStatus.WAIT_SIGN_B.name()) || status.equals(SignContractStatus.WAIT_SIGN_A.name())) {
            signContractResponse.setCanSend(false);
            notificationService.create(Notification.builder()
                    .title(contract.get().getName())
                    .message(email + " đang chờ ký")
                    .typeNotification("CONTRACT")
                    .receivers(receivers)
                    .sender(email)
                    .build());
        }
        contractStatusService.create(email, receivers, contractId, status, description);
        mailService.sendNewMail(to, cc, subject, htmlContent, attachments);
        return signContractResponse;
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
                                                @PathVariable int page, @PathVariable int size) {
        Pageable p = PageRequest.of(page, size);
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(contractService.findAll(p, email, status));
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
        return ResponseEntity.ok(contractService.getContractSignById(id));
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
