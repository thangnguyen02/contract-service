package com.fpt.servicecontract.auth.model;


import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public enum Role {
  USER,
  ADMIN;
  public List<SimpleGrantedAuthority> getAuthorities(String email) {
    List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
    simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE" + "_CREATE_CONTRACT"));

    if(email.equals("tudda@gmail.com")){
      log.info("role user hehe");
      simpleGrantedAuthorities.add(new SimpleGrantedAuthority("PER_CRUD"));
    }
    return simpleGrantedAuthorities;
  }
}
