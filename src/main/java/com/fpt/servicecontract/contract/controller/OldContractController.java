package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.ContractDto;
import com.fpt.servicecontract.contract.dto.CreateUpdateOldContract;
import com.fpt.servicecontract.contract.dto.SearchContractRequest;
import com.fpt.servicecontract.contract.dto.SearchOldContractRequest;
import com.fpt.servicecontract.contract.model.OldContract;
import com.fpt.servicecontract.contract.service.OldContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/old-contract")
@RequiredArgsConstructor
public class OldContractController {
    private final MailService mailService;
    private final OldContractService oldContractService;

    @PostMapping("/search")
    public ResponseEntity<Page<OldContract>> search(@RequestBody SearchOldContractRequest request) {
        Pageable pageable = Pageable.ofSize(request.getSize()).withPage(request.getPage());
        return ResponseEntity.ok(oldContractService.getContracts(pageable));
    }

    @PostMapping()
    public BaseResponse create(
            @RequestHeader("Authorization") String bearerToken,
            @ModelAttribute CreateUpdateOldContract contractDto,
            @RequestParam MultipartFile[] images
            )
    {
          return oldContractService.create(bearerToken, contractDto, images);
    }
}
