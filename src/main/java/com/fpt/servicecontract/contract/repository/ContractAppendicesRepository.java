package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractAppendices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractAppendicesRepository extends JpaRepository<ContractAppendices, String> {
}