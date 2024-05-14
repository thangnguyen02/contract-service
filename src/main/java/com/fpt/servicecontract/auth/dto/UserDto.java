package com.fpt.servicecontract.auth.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private String name;
    private String phone;
}
