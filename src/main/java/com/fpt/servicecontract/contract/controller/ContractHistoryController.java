package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.service.ContractHistoryService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/contract-history")
@RequiredArgsConstructor
public class ContractHistoryController {
    private final ContractHistoryService contractHistoryService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getContractHistory(
            @RequestParam String contract,
            @RequestParam String createdBy){
        return ResponseEntity.ok(contractHistoryService.getContractHistory(contract, createdBy));
    }

}
