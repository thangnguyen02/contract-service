package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.AuthenticationRequest;
import com.fpt.servicecontract.auth.dto.AuthenticationResponse;
import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.model.Item;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.service.AuthenticationService;
import com.fpt.servicecontract.ultils.ExportReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final ExportReport exportReport;


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

  @GetMapping("/item-report/{format}")
  public ResponseEntity<Resource> getItemReport(@PathVariable("format")String format) throws JRException, IOException {

    List<Item> a = new ArrayList<>();
    a.add(new Item("Item 1"));
    a.add(new Item("Item 2"));
    a.add(new Item("Item 3"));
    a.add(new Item("Item 4"));

    byte[] reportContent = exportReport.getItemReport(a, format);

    ByteArrayResource resource = new ByteArrayResource(reportContent);

    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(resource.contentLength())
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    ContentDisposition.attachment()
                            .filename("item-report." + format)
                            .build().toString())
            .body(resource);
  }
}
