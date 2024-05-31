package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractPartyRepository extends JpaRepository<ContractParty, String> {
}
