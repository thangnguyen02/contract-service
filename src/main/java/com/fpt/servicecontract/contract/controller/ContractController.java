package com.fpt.servicecontract.contract.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    @GetMapping("/test-role")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String test() {
        return "ROLE_USER";
    }
    @GetMapping("/test-permission")
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_CONTRACT')")
    public String tests() {
        return "PERMISSION_MANAGE_CONTRACT";
    }
}
