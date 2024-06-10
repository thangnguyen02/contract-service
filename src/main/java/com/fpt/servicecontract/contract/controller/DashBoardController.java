package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.dto.StatisticalRequest;
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

    @GetMapping("/statisticByMonth")
    public ResponseEntity<BaseResponse> statistical(@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                    @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        return ResponseEntity.ok(dashboardService.numberNewContract(fromDate, toDate));
    }


    @GetMapping("/signStatus")
    public ResponseEntity<BaseResponse> statisticalBySignStatus(@RequestParam(value = "status", required = false) String status) {
        return ResponseEntity.ok(dashboardService.numberContractBySignStatus(status));
    }
}
