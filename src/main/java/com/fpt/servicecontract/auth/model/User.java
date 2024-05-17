package com.fpt.servicecontract.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {


  @Id
  @UuidGenerator
  private String id;

  @Column(unique=true)
  private String userCode;

  @Column(unique=true)
  private String identificationNumber;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String phone;

  private String name;
  @JsonIgnore
  private String password;

  private String position;

  private String department;

  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @Enumerated(EnumType.STRING)
  private Role role;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Set<Permission> permissions;

  private LocalDateTime createdDate;

  private LocalDateTime updatedDate;

  private Date dob;

  private boolean gender;

  private String address;

  private String avatar;

//  DELETE FROM `fpt_company`.`user_permissions` WHERE (`user_id` = '7802448b-ef4a-49a4-a358-d67ed7c510ba')

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
    simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.getRole()));
    this.getPermissions().forEach(f -> {
      simpleGrantedAuthorities.add(new SimpleGrantedAuthority(String.valueOf(f.getPermission())));
    });
    return simpleGrantedAuthorities;
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonIgnore
  @Override
  public String getUsername() {
    return email;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }


}
