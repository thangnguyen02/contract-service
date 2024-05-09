package com.fpt.servicecontract.auth.repository;

import com.fpt.servicecontract.auth.model.Permission;
import com.fpt.servicecontract.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
}
