package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.AuthenticationRequest;
import com.fpt.servicecontract.auth.dto.AuthenticationResponse;
import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;


  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

}
