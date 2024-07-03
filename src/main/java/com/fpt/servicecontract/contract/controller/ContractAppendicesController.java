package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.model.ContractAppendices;
import com.fpt.servicecontract.contract.service.ContractAppendicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contract-appendices")
public class ContractAppendicesController {
    @Autowired
    private ContractAppendicesService service;

    @GetMapping
    public List<ContractAppendices> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractAppendices> getById(@PathVariable String id) {
        Optional<ContractAppendices> contractAppendices = service.getById(id);
        if (contractAppendices.isPresent()) {
            return ResponseEntity.ok(contractAppendices.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ContractAppendices create(@RequestBody ContractAppendices contractAppendices) {
        return service.save(contractAppendices);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractAppendices> update(@PathVariable String id, @RequestBody ContractAppendices contractAppendices) {
        Optional<ContractAppendices> existingContractAppendices = service.getById(id);
        if (existingContractAppendices.isPresent()) {
            contractAppendices.setId(id);
            return ResponseEntity.ok(service.save(contractAppendices));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.getById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
