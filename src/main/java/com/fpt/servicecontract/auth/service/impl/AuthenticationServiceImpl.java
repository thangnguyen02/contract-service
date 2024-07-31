package com.fpt.servicecontract.auth.service.impl;

import com.fpt.servicecontract.auth.dto.AuthenticationRequest;
import com.fpt.servicecontract.auth.dto.AuthenticationResponse;
import com.fpt.servicecontract.auth.dto.ChangePasswordRequest;
import com.fpt.servicecontract.auth.model.UserStatus;
import com.fpt.servicecontract.auth.repository.UserRepository;
import com.fpt.servicecontract.auth.service.AuthenticationService;
import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.utils.BaseResponse;
import com.fpt.servicecontract.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        user.setTokenDevice(request.getTokenDevice() == null ? user.getTokenDevice() : request.getTokenDevice());
        repository.save(user);

        if(user.getStatus() != UserStatus.ACTIVE) {
            throw new AuthenticationServiceException("Account is not active");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtToken);
//    authenticationResponse.setRefreshToken(refreshToken);
        authenticationResponse.setUser(user);
        return authenticationResponse;
    }

    public BaseResponse logout(String email) {
        var userOptional = repository.findByEmail(email);
        if(userOptional.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.SUCCESS, "User not valid", true, null);
        }
        userOptional.get().setTokenDevice(null);
        repository.save(userOptional.get());
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "User successfully logged out", true, null);
    }

    @Override
    public BaseResponse changePassword(ChangePasswordRequest request) {
        var user = repository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return new BaseResponse(Constants.ResponseCode.NOT_FOUND, "user not found", true, null);
        }
        if (passwordEncoder.matches(request.getOldPassword(), user.get().getPassword())) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, "Old password wrong", true, null);
        }
        user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
        try {
            repository.save(user.get());
        } catch (Exception e) {
            return new BaseResponse(Constants.ResponseCode.FAILURE, e.getMessage(), false, null);
        }
        return new BaseResponse(Constants.ResponseCode.SUCCESS, "Change password successfully", true, null);
    }
}
