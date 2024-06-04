package com.fpt.servicecontract.contract.repository;

import com.fpt.servicecontract.contract.model.ContractHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractHistoryRepository extends JpaRepository<ContractHistory, String> {

    @Query(value = """
        select u.id, u.contract_id, u.created_by, u.created_date, u.status from contract_history
        u where 1=1 and u.contract_id = :contractId
    """, nativeQuery = true)
    List<Object[]> getContractHistoriesByContractId(@Param("contractId") String contractId);
}
