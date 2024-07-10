package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.model.ContractType;
import com.fpt.servicecontract.contract.service.ContractTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/contract-type")
@RequiredArgsConstructor
public class ContractTypeController {
    @Autowired
    private ContractTypeService contractTypeService;

    @GetMapping
    public ResponseEntity<Page<ContractType>> getAllContractTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title
            ) {
        Page<ContractType> contractTypes = contractTypeService.getAllContractTypes(page, size, title);
        return ResponseEntity.ok(contractTypes);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ContractType> getContractTypeById(@PathVariable String id) {
        Optional<ContractType> contractType = contractTypeService.getContractTypeById(id);

        return contractType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ContractType createContractType(@RequestBody ContractType contractType) {
        return contractTypeService.createContractType(contractType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractType> updateContractType(@PathVariable String id, @RequestBody ContractType contractTypeDetails) {
        try {
            ContractType updatedContractType = contractTypeService.updateContractType(id, contractTypeDetails);
            return ResponseEntity.ok(updatedContractType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractType(@PathVariable String id) {
        contractTypeService.deleteContractType(id);
        return ResponseEntity.noContent().build();
    }

}
