package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.dto.ContractRequest;
import com.fpt.servicecontract.contract.dto.ContractTemplateRequest;
import com.fpt.servicecontract.contract.service.ContractTemplateService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/contract-templates")
@RequiredArgsConstructor
public class ContractTemplateController {
    private final ContractTemplateService contractTemplateService;

    @GetMapping("/{page}/{size}")
    public ResponseEntity<BaseResponse> findAll(@PathVariable int page, @PathVariable int size) {
        Pageable p = PageRequest.of(page, size);
        return ResponseEntity.ok(contractTemplateService.finAllTemplates(p));
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> create(@RequestBody ContractTemplateRequest contractRequest) {
        return ResponseEntity.ok(contractTemplateService.createContract(contractRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(contractTemplateService.delete(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable String id,
                                                       @RequestBody ContractTemplateRequest contractRequest) {
        return ResponseEntity.ok(contractTemplateService.update(id, contractRequest));
    }
}
