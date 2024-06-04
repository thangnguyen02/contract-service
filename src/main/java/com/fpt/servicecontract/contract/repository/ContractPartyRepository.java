package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractPartyRepository extends JpaRepository<ContractParty, String> {
    Optional<ContractParty> findByTaxNumber(String id);
}
