package com.fpt.servicecontract.auth.service;


import com.fpt.servicecontract.auth.dto.AuthenticationRequest;
import com.fpt.servicecontract.auth.dto.AuthenticationResponse;
import com.fpt.servicecontract.utils.BaseResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    BaseResponse logout(String email);
}
