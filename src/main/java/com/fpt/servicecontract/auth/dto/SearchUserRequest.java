package com.fpt.servicecontract.auth.dto;

import com.fpt.servicecontract.auth.model.UserStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class SearchUserRequest {
    private String name;
    private String email;
    private String status;
    private String identificationNumber;
    private String phoneNumber;
    private String address;
    private String department;
    private String position;
}
