package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.AuthenticationRequest;
import com.fpt.servicecontract.auth.dto.AuthenticationResponse;
import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PostMapping("/register-for-user")
  public ResponseEntity<String> register(
      @RequestBody RegisterRequest request
  ) {
    if (Role.ADMIN.equals(request.getRole())) {
      log.warn("Admin is not created");
      return ResponseEntity.ofNullable("Failed to created as role ADMIN");
    }
    return ResponseEntity.ok(service.register(request));
  }

  @DeleteMapping("/delete-user/{id}")
  @PreAuthorize("hasAuthority('PERMISSION_MANAGE_USER')")
  public ResponseEntity<String> delete(@PathVariable("id")String id)
  {
    return ResponseEntity.ok(service.delete(id));
  }

}
