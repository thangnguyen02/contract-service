package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.AuthenticationRequest;
import com.fpt.servicecontract.auth.dto.AuthenticationResponse;
import com.fpt.servicecontract.auth.dto.ChangePasswordRequest;
import com.fpt.servicecontract.auth.service.AuthenticationService;
import com.fpt.servicecontract.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/logout")
  public ResponseEntity<BaseResponse> logout(@RequestParam String email) {
    return ResponseEntity.ok(service.logout(email));
  }

  @PostMapping("/change-password")
  public ResponseEntity<BaseResponse> changePassword(@RequestBody ChangePasswordRequest authenticationRequest) {
    return ResponseEntity.ok(service.changePassword(authenticationRequest));
  }
}
