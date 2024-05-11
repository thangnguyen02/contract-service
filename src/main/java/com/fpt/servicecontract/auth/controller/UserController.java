package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.UpdateUserRequest;
import com.fpt.servicecontract.auth.dto.UserDto;
import com.fpt.servicecontract.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @DeleteMapping("/delete-user/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE_USER')")
    public ResponseEntity<String> delete(@PathVariable("id")String id)
    {
        return ResponseEntity.ok(service.delete(id));
    }

    @PutMapping("/update-user/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_USER')")
    public ResponseEntity<UserDto> update(@PathVariable("id")String id, @RequestBody UpdateUserRequest user)
    {
        return ResponseEntity.ok(service.update(id, user));
    }
}
