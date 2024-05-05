package com.fpt.servicecontract.contract.Controller;

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

  @GetMapping("/test")
  @PreAuthorize("hasRole('ROLE_CREATE_CONTRACT')")
  public String test() {
    return "test";
  }
  @GetMapping("/tests")
  @PreAuthorize("hasAuthority('PER_CRUD')")
  public String tests() {
    return "ascasc";
  }
}
