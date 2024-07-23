package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.contract.service.PaySlipService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

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
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate
    ) {
        return ResponseEntity.ok(paySlipService.GetAllPaySlip(Pageable.ofSize(size).withPage(page), fromDate, toDate));
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
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestHeader("Authorization") String bearerToken) {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(paySlipService.GetPaySlipById(Pageable.ofSize(size).withPage(page), fromDate, toDate, email));
    }


}
