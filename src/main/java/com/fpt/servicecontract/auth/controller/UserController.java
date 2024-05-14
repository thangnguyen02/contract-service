package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.SearchUserRequest;
import com.fpt.servicecontract.auth.dto.UpdateUserRequest;
import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.auth.dto.UserInterface;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_USER')")
    public ResponseEntity<String> delete(@PathVariable("id")String id)
    {
        return ResponseEntity.ok(service.delete(id));
    }

    @PutMapping("/{id}")

    @Transactional(rollbackOn = Exception.class)
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_USER')")
    public ResponseEntity<UserDto> update(@PathVariable("id")String id, @RequestBody UpdateUserRequest user)
    {
        return ResponseEntity.ok(service.update(id, user));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserInterface>> getAll(@ModelAttribute SearchUserRequest  userRequest, Pageable pageable) {
        return ResponseEntity.ok(service.search(userRequest, pageable));
    }
}
