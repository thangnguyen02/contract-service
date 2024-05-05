package com.fpt.servicecontract.auth.dto;


import com.fpt.servicecontract.auth.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

  private String name;
  private String email;
  private String password;
  private Role role;
  private String phone;
  private String position;
  private String department;

}
