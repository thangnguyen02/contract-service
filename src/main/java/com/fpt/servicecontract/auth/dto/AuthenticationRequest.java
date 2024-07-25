package com.fpt.servicecontract.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

  private String email;
  private String password;
  private String tokenDevice;
}
