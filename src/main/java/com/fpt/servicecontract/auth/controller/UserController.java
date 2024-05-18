package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.dto.SearchUserRequest;
import com.fpt.servicecontract.auth.dto.UpdateUserRequest;
import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.auth.dto.UserInterface;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")String id)
    {
        return ResponseEntity.ok(service.delete(id));
    }
    @PostMapping("/register-for-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> register(
        @RequestBody RegisterRequest request
    ) throws AuthenticationException {
        if (Role.ADMIN.equals(request.getRole())) {
            log.warn("Admin is not created");
            throw new AuthenticationException();
        }
        return ResponseEntity.ok(service.register(request));
    }
    @PutMapping("/{id}")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<UserDto> update(
            @PathVariable("id") String id,
            @ModelAttribute UpdateUserRequest user,
            @RequestParam(value = "file", required = false) MultipartFile file
            )
    {
        try {
            return ResponseEntity.ok(service.update(id, user, file));
        } catch (IOException e) {
            throw new RuntimeException("Data invalid");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserInterface>> getAll(@ModelAttribute SearchUserRequest  userRequest, Pageable pageable) {
        return ResponseEntity.ok(service.search(userRequest, pageable));
    }
}
