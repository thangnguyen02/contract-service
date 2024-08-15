package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.contract.service.DashboardService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistic")
    public ResponseEntity<BaseResponse> statistical(@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                    @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                    @RequestParam(value = "signStatus", required = false) String status) {
        return ResponseEntity.ok(dashboardService.numberNewContract(fromDate, toDate, status));
    }

    @GetMapping("/sale")
    public ResponseEntity<BaseResponse> getNumberSale(
            @RequestParam String userEmail,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(dashboardService.getNumberSale(userEmail, status));
    }

    private final JwtService jwtService;

    @GetMapping("/count-reason/{sale}/{number}")
    public ResponseEntity<BaseResponse> reason(@RequestHeader("Authorization") String bearerToken, @PathVariable int sale, @PathVariable int number) {
        String email = jwtService.extractUsername(bearerToken.substring(7));
        return ResponseEntity.ok(dashboardService.countReason(email, sale, number));
    }
}
