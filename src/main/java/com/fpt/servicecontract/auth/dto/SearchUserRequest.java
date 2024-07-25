package com.fpt.servicecontract.auth.dto;

import lombok.Getter;
import lombok.Setter;

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
