package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatus, String> {
    Optional<ContractStatus> findByContractId(String id);
}
