package com.fpt.servicecontract.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.servicecontract.auth.model.User;
import lombok.Data;

@Data
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
//  @JsonProperty("refresh_token")
//  private String refreshToken;
  @JsonProperty("user")
  private User user;

}
