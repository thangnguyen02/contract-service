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

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    authenticationResponse.setAccessToken(jwtToken);
    authenticationResponse.setRefreshToken(refreshToken);
    return authenticationResponse;
  }

  public String register(RegisterRequest request) {
    var user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPhone(request.getPhone());
    user.setDepartment(request.getDepartment());
    user.setPosition(request.getPosition());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(request.getRole());
    user.setPermissions(request.getPermissions());
    repository.save(user);
    return "Successfully";
  }

  public String delete(String id) {
    var user = repository.findById(id).orElseThrow();

    user.setStatus(UserStatus.ACTIVE.getUserStatus());
    repository.save(user);
    return "Successfully";
  }

}
