package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.contract.model.Reason;
import com.fpt.servicecontract.contract.service.ReasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/reason")
@RequiredArgsConstructor
public class ReasonController {
    private final ReasonService ReasonService;

    @GetMapping
    public ResponseEntity<Page<Reason>> getAllReasons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title
            ) {
        Page<Reason> Reasons = ReasonService.getAllReasons(page, size, title);
        return ResponseEntity.ok(Reasons);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Reason> getReasonById(@PathVariable String id) {
        Optional<Reason> Reason = ReasonService.getReasonById(id);

        return Reason.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reason createReason(@RequestBody Reason Reason) {
        return ReasonService.createReason(Reason);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reason> updateReason(@PathVariable String id, @RequestBody Reason ReasonDetails) {
        try {
            Reason updatedReason = ReasonService.updateReason(id, ReasonDetails);
            return ResponseEntity.ok(updatedReason);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReason(@PathVariable String id) {
        ReasonService.deleteReason(id);
        return ResponseEntity.noContent().build();
    }

}
