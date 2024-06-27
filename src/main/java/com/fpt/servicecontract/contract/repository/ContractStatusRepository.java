package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatus, String> {
    Optional<ContractStatus> findByContractId(String id);
    @Query(value = """
        SELECT status
        FROM contract_status
        where contract_id = ?1
        ORDER BY send_date DESC
        LIMIT 1   
    """, nativeQuery = true)
    String findByContractLastStatus(String contractId);


    @Query(value = """
        SELECT status
        FROM contract_status
        where contract_id = ?1
        ORDER BY send_date DESC
    """, nativeQuery = true)
    List<String> checkDoneSign(String contractId);
}
