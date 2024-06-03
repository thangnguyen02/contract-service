package com.fpt.servicecontract.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.servicecontract.auth.model.Permission;
import com.fpt.servicecontract.auth.model.Role;
import com.fpt.servicecontract.auth.model.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private String id;
    private String userCode;
    private String identificationNumber;
    private String email;
    private String phone;
    private String name;
    private String password;
    private String position;
    private String department;
    private UserStatus status;
    private Role role;
    private Set<Permission> permissions;
    private String createdDate;
    private String updatedDate;
    private String dob;
    private boolean gender;
    private String address;
    private String avatar;
}
