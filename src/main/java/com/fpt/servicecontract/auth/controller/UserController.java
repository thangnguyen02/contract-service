package com.fpt.servicecontract.auth.controller;

import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.dto.SearchUserRequest;
import com.fpt.servicecontract.auth.dto.UpdateUserRequest;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.service.UserService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BaseResponse delete(@PathVariable("id")String id)
    {
        return service.delete(id);
    }

    @PostMapping("/register-for-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BaseResponse register(@RequestBody RegisterRequest request) {
        if (Role.ADMIN.equals(request.getRole())) {
            log.warn("Admin is not created");
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "You not have permission", true, null);
        }
        return service.register(request);
    }

    @PutMapping("/{id}")
    @Transactional(rollbackOn = Exception.class)
    public BaseResponse update(
            @PathVariable("id") String id,
            @ModelAttribute UpdateUserRequest user,
            @RequestParam(value = "file", required = false) MultipartFile file
            )
    {
            return service.update(id, user, file);
    }

    @GetMapping("/search")
    public BaseResponse getAll(@ModelAttribute SearchUserRequest  userRequest, Pageable pageable) {
        return service.search(userRequest, pageable);
    }

    @GetMapping("/{id}")
    public BaseResponse findById(@PathVariable("id")String id) {
        return service.getUserById(id);
    }

    @GetMapping("/searchByPermission")
    public BaseResponse getAll(@RequestParam("permission") String permission, Pageable pageable) {
        return service.getUserByPermission(permission, pageable);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse> resetPassword(@RequestParam String email) {
        return ResponseEntity.ok(service.resetPass(email));
    }
}
