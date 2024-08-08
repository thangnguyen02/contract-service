package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.dto.request.VerifyEmailCodeRequest;
import com.fpt.servicecontract.contract.service.MailAuthenCodeService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public/otp")
@RequiredArgsConstructor
public class OtpController {

    private final MailAuthenCodeService mailAuthenCodeService;

    @GetMapping("/{email}/{type}/{contractId}")
    public BaseResponse getOtpByEmail(
            @PathVariable String email,
            @PathVariable String contractId,
            @PathVariable String type
    ) {
        return mailAuthenCodeService.GetAuthenMailCode(email, contractId, type);
    }

    @PostMapping("/get-contract")
    public BaseResponse getContract(@RequestBody VerifyEmailCodeRequest verifyEmailCodeRequest) {
        return mailAuthenCodeService.AuthenticationMailWithCode(verifyEmailCodeRequest.getEmail(), verifyEmailCodeRequest.getCode());
    }


}
