package com.fpt.servicecontract.auth.model;

import com.fpt.servicecontract.auth.repository.UserRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {


    @Id
    @UuidGenerator
    private String id;

    private String name;

    private String email;

    private String phone;

    private String password;

    private String position;

    private String department;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

//  DELETE FROM `fpt_company`.`user_permissions` WHERE (`user_id` = '7802448b-ef4a-49a4-a358-d67ed7c510ba')

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.getRole()));
        this.getPermissions().forEach(f -> {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(String.valueOf(f.getPermission())));
        });
        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
