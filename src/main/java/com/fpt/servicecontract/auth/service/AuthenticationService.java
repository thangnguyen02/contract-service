package com.fpt.servicecontract.auth.service;


import com.fpt.servicecontract.auth.dto.AuthenticationRequest;
import com.fpt.servicecontract.auth.dto.AuthenticationResponse;
import com.fpt.servicecontract.auth.dto.RegisterRequest;
import com.fpt.servicecontract.auth.model.UserStatus;
import com.fpt.servicecontract.config.JwtService;
import com.fpt.servicecontract.auth.model.User;
import com.fpt.servicecontract.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository repository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();

    if(user.getStatus() != UserStatus.ACTIVE) {
      throw new AuthenticationServiceException("Account is not active");
    }

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    authenticationResponse.setAccessToken(jwtToken);
    authenticationResponse.setRefreshToken(refreshToken);
    authenticationResponse.setUser(user);
    return authenticationResponse;
  }
}
