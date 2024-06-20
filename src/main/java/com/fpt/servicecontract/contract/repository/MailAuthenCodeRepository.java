package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.AuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailAuthenCodeRepository extends JpaRepository<AuthenticationCode, String> {

    Optional<AuthenticationCode> findByEmail(String code);
}
