package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.MailAuthedCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailAuthenCodeRepository extends JpaRepository<MailAuthedCode, String> {

    Optional<MailAuthedCode> findByEmail(String code);
}
