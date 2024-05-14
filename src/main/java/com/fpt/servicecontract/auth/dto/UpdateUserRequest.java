package com.fpt.servicecontract.auth.dto;

import com.fpt.servicecontract.auth.model.Permission;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String name;
    private String password;
    private Role role;
    private String phone;
    private String position;
    private String department;
    private UserStatus status;
    private Set<Permission> permissions;
}
