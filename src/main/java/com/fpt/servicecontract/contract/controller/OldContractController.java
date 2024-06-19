package com.fpt.servicecontract.contract.controller;

import com.fpt.servicecontract.config.MailService;
import com.fpt.servicecontract.contract.dto.CreateUpdateOldContract;
import com.fpt.servicecontract.contract.dto.SearchRequestBody;
import com.fpt.servicecontract.contract.model.OldContract;
import com.fpt.servicecontract.contract.service.ElasticSearchService;
import com.fpt.servicecontract.contract.service.OldContractService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/old-contract")
@RequiredArgsConstructor
public class OldContractController {
    private final MailService mailService;
    private final OldContractService oldContractService;
    private final ElasticSearchService elasticSearchService;
    @GetMapping()
    public BaseResponse getAll(@RequestParam("page") int page, @RequestParam("size") int size) {

        return oldContractService.getContracts(page, size);
    }

    @PostMapping()
    public BaseResponse create(
            @RequestHeader("Authorization") String bearerToken,
            @ModelAttribute CreateUpdateOldContract contractDto,
            @RequestParam MultipartFile[] images
            ) throws Exception {
          return oldContractService.create(bearerToken, contractDto, images);
    }
    @PostMapping("/mobile")
    public BaseResponse createWithMobile(
            @RequestHeader("Authorization") String bearerToken,
            @ModelAttribute CreateUpdateOldContract contractDto,
            @RequestParam(required = false) String[] images
    ) throws Exception {
        return oldContractService.createWithMobile(bearerToken, contractDto, images);
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(
            @PathVariable("id") String id
    ) throws IOException {
        return oldContractService.delete(id);
    }
    @PostMapping("/search")
    public ResponseEntity<BaseResponse> searchContracts(@RequestBody SearchRequestBody searchRequestBody) throws IOException {
        return ResponseEntity.ok(new BaseResponse(Constants.ResponseCode.SUCCESS,
                "Successfully", true, elasticSearchService.search("old_contract", searchRequestBody, OldContract.class)));
    }
    @GetMapping("/sync")
    public ResponseEntity<Void> sync() throws IOException {
        return ResponseEntity.ok(oldContractService.sync());
    }
}
