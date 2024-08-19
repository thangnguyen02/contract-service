package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.dto.request.ContractRequest;
import com.fpt.servicecontract.contract.dto.request.SearchRequestBody;
import com.fpt.servicecontract.contract.model.Party;
import com.fpt.servicecontract.contract.service.ContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController {
    private final ContractService contractService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getParty() throws IOException {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, contractService.getDefaultParty()));
    }
    @GetMapping("/find-all")
    public ResponseEntity<BaseResponse> getAllParty() throws IOException {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, contractService.getAll()));
    }
    @PostMapping()
    public ResponseEntity<BaseResponse> createParty(@RequestBody Party party) throws IOException {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, contractService.createDefaultParty(party)));
    }

}
