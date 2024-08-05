package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.dto.request.PaySlipFormulaUpdateRequest;
import com.fpt.servicecontract.contract.model.PaySlipFormula;
import com.fpt.servicecontract.contract.service.PaySlipFormulaService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pay-slip-formula")
@RequiredArgsConstructor
public class PaySlipFormulaController {
    private final PaySlipFormulaService paySlipFormulaService;

    @GetMapping("/{page}/{size}")
    public ResponseEntity<BaseResponse> getAllPaySlipFormula(
            @PathVariable int page,
            @PathVariable int size
    ) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ResponseEntity.ok(paySlipFormulaService.getAll(pageable));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createPaySlipFormula(
            @RequestBody PaySlipFormula paySlipFormula
    ) {
        return ResponseEntity.ok(paySlipFormulaService.create(paySlipFormula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePaySlipFormula(
            @PathVariable String id,
            @RequestBody List<PaySlipFormulaUpdateRequest> paySlipFormula
    ) {
        return ResponseEntity.ok(paySlipFormulaService.update(id, paySlipFormula));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePaySlipFormula(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(paySlipFormulaService.delete(id));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<BaseResponse> findById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(paySlipFormulaService.getById(id));
    }
}
