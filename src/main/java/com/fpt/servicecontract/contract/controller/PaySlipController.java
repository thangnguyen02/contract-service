package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.contract.service.PaySlipService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/pay-slip")
@RequiredArgsConstructor
public class PaySlipController {
    private final PaySlipService paySlipService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<BaseResponse> paySlip(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String type
    ) {
        return ResponseEntity.ok(paySlipService.GetAllPaySlip(Pageable.ofSize(size).withPage(page), month, year, type, null));
    }

    @GetMapping("/calculate")
    public ResponseEntity<BaseResponse> calculatePaySlip(
    ) {
        return ResponseEntity.ok(paySlipService.CalculateAllPaySlip());
    }

    @GetMapping("/findByMail")
    public ResponseEntity<BaseResponse> paySlipByEmail(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestHeader("Authorization") String bearerToken) {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(paySlipService.GetAllPaySlip(Pageable.ofSize(size).withPage(page), month, year, null, email));
    }

    @GetMapping("/commission")
    public ResponseEntity<BaseResponse> getCommission() {
        return ResponseEntity.ok(paySlipService.GetCommission());
    }


}
